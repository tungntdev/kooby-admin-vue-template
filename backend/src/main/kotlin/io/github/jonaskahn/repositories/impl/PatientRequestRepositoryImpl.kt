package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.constants.Defaults
import io.github.jonaskahn.controllers.patientrequest.ReportResponse
import io.github.jonaskahn.entities.Assignment
import io.github.jonaskahn.entities.PatientRequest
import io.github.jonaskahn.entities.User
import io.github.jonaskahn.entities.enums.State
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.middlewares.context.UserContextHolder
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.PatientRequestRepository
import io.github.jonaskahn.services.patientrequest.PatientRequestDto
import io.github.jonaskahn.services.patientrequest.PatientRequestEntityToDtoMapper
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class PatientRequestRepositoryImpl @Inject constructor(
    override val entityManager: EntityManager
) : AbstractBaseRepository(entityManager), PatientRequestRepository {
    override fun create(entity: PatientRequest) {
        entityManager.persist(entity)
    }

    override fun update(entity: PatientRequest) {
        entityManager.merge(entity)
    }

    override fun delete(id: Int) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            entity.status = Status.DELETED
            entityManager.merge(entity)
        }
    }

    override fun countByKeywordAndState(
        keyword: String?,
        state: Collection<State>,
        copyUser: Int?
    ): Long {
        val likeKeyword = "%${keyword?.trim()}%"
        var countQueryStr = """
            SELECT COUNT(pr) FROM PatientRequest pr
            LEFT JOIN Assignment a ON a.idPatientRequest =pr
            WHERE (pr.patientNumber LIKE :keyword 
                OR pr.patientName LIKE :keyword 
                OR pr.medicineCode LIKE :keyword)
                AND pr.state in :states
                AND pr.status = :status
        """

        if (copyUser != null) {
            countQueryStr += " AND a.idCopyUser = :copyUser"
        }

        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("keyword", likeKeyword)
        countQuery.setParameter("states", state)
        countQuery.setParameter("status", Status.ACTIVATED)
        if (copyUser != null) {
            val id = UserContextHolder.getCurrentUserId()
            countQuery.setParameter("copyUser", id)
        }
        return countQuery.singleResult
    }

    override fun searchByKeywordAndStateAndOffset(
        keyword: String?,
        state: Collection<State>,
        offset: Long,
        copyUser: Int?
    ): Collection<PatientRequestDto> {
        val likeKeyword = "%${keyword?.trim()}%"

        var queryStr = """
            SELECT pr, a, u FROM PatientRequest pr
            LEFT JOIN Assignment a ON a.idPatientRequest =pr
            LEFT JOIN users u on a.idCopyUser = u.id
            WHERE ( pr.patientNumber LIKE :keyword 
                OR pr.patientName LIKE :keyword 
                OR pr.medicineCode LIKE :keyword)
                AND pr.state in :states
                AND pr.status = :status
        """
        if (copyUser != null) {
            queryStr += " AND a.idCopyUser = :copyUser"
        }
        queryStr += " ORDER BY pr.id DESC"

        val query = entityManager.createQuery(queryStr, Array<Any>::class.java)
        query.setParameter("keyword", likeKeyword)
        query.setParameter("states", state)
        query.setParameter("status", Status.ACTIVATED)
        if (copyUser != null) {
            val id = UserContextHolder.getCurrentUserId()
            query.setParameter("copyUser", id)
        }
        query.firstResult = offset.toInt()
        query.maxResults = Defaults.Pageable.DEFAULT_PAGE_SIZE

        val results = query.resultList

        return results.map { result ->
            val patientRequest = result[0] as PatientRequest
            val assignment = result[1] as? Assignment
            val user = result[2] as? User
            PatientRequestEntityToDtoMapper.INSTANCE.toDto(patientRequest, assignment, user)
        }
    }

    override fun findNextPatientOrder(): Long {
        val countQueryStr = """
            SELECT COUNT(pr) FROM PatientRequest pr
            WHERE YEAR(pr.receptionDate) = :year 
                AND pr.status = :status
        """
        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("status", Status.ACTIVATED)
        countQuery.setParameter("year", Instant.now().atZone(ZoneId.systemDefault()).year)
        return countQuery.singleResult + 1
    }

    override fun findNextPatientDeliveryNumber(): Long {
        val countQueryStr = """
            SELECT COUNT(pr) FROM PatientRequest pr
            WHERE YEAR(pr.receptionDate) = :year 
                AND pr.status = :status
                AND pr.delivery = :delivery
        """
        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("status", Status.ACTIVATED)
        countQuery.setParameter("delivery", 1)
        countQuery.setParameter("year", Instant.now().atZone(ZoneId.systemDefault()).year)
        return countQuery.singleResult + 1
    }

    override fun setSigned(id: Int) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            entity.signDate = Instant.now()
            entity.state = State.DIRECTOR_APPROVED
            entityManager.merge(entity)
        }
    }

    override fun setReceived(id: Int) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            if (entity.signDate == null) {
                entity.signDate = Instant.now()
            }
            if (entity.donePatientDate == null) {
                entity.donePatientDate = Instant.now()
            }
            entity.donePatientDate = Instant.now()
            entity.state = State.COMPLETE
            entityManager.merge(entity)
        }
    }

    override fun setInProgress(id: Long) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            entity.state = State.IN_PROGRESS
            entityManager.merge(entity)
        }
    }

    override fun setDelivered(id: Int) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            if (entity.donePatientDate == null) {
                entity.donePatientDate = Instant.now()
            }
            if (entity.signDate == null) {
                entity.signDate = Instant.now()
            }
            entity.deliveryDate = Instant.now()
            entity.state = State.COMPLETE
            entityManager.merge(entity)
        }
    }

    override fun setAssignment(id: Int, idCopyUser: Int, appointmentDate: LocalDate) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            entity.state = State.ASSIGNED
            entityManager.merge(entity)
            var assignmentAvailable: Assignment?
            try {
                val queryStr =
                    "SELECT a FROM Assignment a WHERE a.idPatientRequest = :idPatientRequest AND a.status = :status"
                val query = entityManager.createQuery(queryStr, Assignment::class.java)
                query.setParameter("idPatientRequest", entity)
                query.setParameter("status", Status.ACTIVATED)
                assignmentAvailable = query.singleResult
            } catch (e: NoResultException) {
                assignmentAvailable = null
            }

            if (assignmentAvailable != null) {
                assignmentAvailable.idPatientRequest = entity
                assignmentAvailable.idCopyUser = idCopyUser
                assignmentAvailable.appointmentDate = appointmentDate
                entityManager.merge(assignmentAvailable)
            } else {
                val newAssignment = Assignment().apply {
                    this.idPatientRequest = entity
                    this.idCopyUser = idCopyUser
                    this.appointmentDate = appointmentDate
                }
                entityManager.persist(newAssignment)
            }
        }
    }

    private fun generateReport(
        startDate: LocalDate?,
        endDate: LocalDate?,
        additionalConditions: String = ""
    ): Collection<ReportResponse> {
        val startInstant = startDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
        val endInstant = endDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()

        val baseQuery = """
        SELECT pr FROM PatientRequest pr
        WHERE pr.status = :status
        $additionalConditions
    """

        val query = entityManager.createQuery(baseQuery, PatientRequest::class.java)
        query.setParameter("status", Status.ACTIVATED)
        query.setParameter("startDate", startInstant)
        query.setParameter("endDate", endInstant)

        val results = query.resultList
        val reportResponses = PatientRequestEntityToDtoMapper.INSTANCE.toReportResponse(results)
        reportResponses.forEachIndexed { index, reportResponse ->
            reportResponse.order = index + 1
        }
        return reportResponses
    }

    override fun patientRequestReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return generateReport(startDate, endDate, " AND pr.receptionDate BETWEEN :startDate AND :endDate")
    }

    override fun deliveryReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return generateReport(
            startDate,
            endDate,
            "AND pr.receptionDate BETWEEN :startDate AND :endDate AND pr.delivery = 1"
        )
    }

    override fun deliveredReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return generateReport(
            startDate,
            endDate,
            " AND pr.delivery = 1 AND pr.deliveryDate BETWEEN :startDate AND :endDate"
        )
    }

}