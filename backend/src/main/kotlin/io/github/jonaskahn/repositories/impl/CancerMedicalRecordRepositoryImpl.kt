package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.constants.Defaults
import io.github.jonaskahn.entities.CancerMedicalRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.CancerMedicalRecordRepository
import io.github.jonaskahn.services.storage.CancerMedicalRecordDto
import io.github.jonaskahn.services.storage.CancerMedicalRecordMapper
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import java.time.Instant
import java.time.ZoneId

class CancerMedicalRecordRepositoryImpl @Inject constructor(
    override val entityManager: EntityManager
) : AbstractBaseRepository(entityManager), CancerMedicalRecordRepository {
    override fun create(cancerMedicalRecord: CancerMedicalRecord) {
        cancerMedicalRecord.status = Status.ACTIVATED
        entityManager.persist(cancerMedicalRecord)
    }

    override fun update(cancerMedicalRecord: CancerMedicalRecord) {
        val exitingRecord = entityManager.find(CancerMedicalRecord::class.java, cancerMedicalRecord.id)

        cancerMedicalRecord.status = exitingRecord.status
        cancerMedicalRecord.createdAt = exitingRecord.createdAt
        cancerMedicalRecord.createdBy = exitingRecord.createdBy

        entityManager.merge(cancerMedicalRecord)
    }

    override fun delete(id: Long) {
        val entity = entityManager.find(CancerMedicalRecord::class.java, id)
        if (entity != null) {
            entity.status = Status.DELETED
            entityManager.merge(entity)
        }
    }

    override fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        department: String?,
        status: Collection<Status>,
        offset: Long
    ): List<CancerMedicalRecordDto> {
        val likeKeyword = "%${keyword?.trim()}%"
        val hasDepartment = !department.isNullOrBlank()

        val queryStr = """
        SELECT cmr FROM CancerMedicalRecord cmr
        WHERE cmr.patientName LIKE :keyword
        ${if (hasDepartment) "AND cmr.department = :department" else ""}
        AND cmr.status IN (:status)
        ORDER BY cmr.id DESC
    """

        val query = entityManager.createQuery(queryStr, CancerMedicalRecord::class.java).apply {
            setParameter("keyword", likeKeyword)
            setParameter("status", status)
            if (hasDepartment) setParameter("department", department)
            firstResult = offset.toInt()
            maxResults = Defaults.Pageable.DEFAULT_PAGE_SIZE
        }

        return CancerMedicalRecordMapper.INSTANCE.cancerRecordsToDtos(query.resultList)
    }


    override fun countByKeywordAndStatus(
        keyword: String?, status:
        Collection<Status>,
        department: String?
    ): Long {
        val likeKeyword = "%${keyword?.trim()}%"
        val hasDepartment = !department.isNullOrEmpty()

        val countQueryStr = """
        SELECT COUNT(cmr) FROM CancerMedicalRecord cmr
        WHERE cmr.patientName LIKE :keyword
        ${if (hasDepartment) "AND cmr.department = :department" else ""}
        AND cmr.status IN :status
    """

        return entityManager.createQuery(countQueryStr, Long::class.java).apply {
            setParameter("keyword", likeKeyword)
            setParameter("status", status)
            if (hasDepartment) setParameter("department", department)
        }.singleResult
    }


    override fun findNextCancerMedical(department: String): Long {
        val countQueryStr = """
        SELECT COUNT(cmr) FROM  CancerMedicalRecord  cmr
        WHERE  cmr.status = :status
        AND cmr.department = :department
        AND cmr.saveYear = :year
    """
        val countQuery = entityManager.createQuery(countQueryStr, Long::class.java)
        countQuery.setParameter("status", Status.ACTIVATED)
        countQuery.setParameter("department", department)
        countQuery.setParameter("year", Instant.now().atZone(ZoneId.systemDefault()).year)
        val result = countQuery.singleResult
        return result + 1
    }
}