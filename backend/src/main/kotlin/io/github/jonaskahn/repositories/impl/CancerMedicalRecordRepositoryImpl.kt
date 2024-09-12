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
//        cancerMedicalRecord.createdAt = Instant.now()
//        cancerMedicalRecord.createdBy = UserContextHolder.getCurrentUserId()
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

    override fun countByKeywordAndStatus(keyword: String?, status: Collection<Status>): Long {
        val likeKeyword = "%${keyword?.trim()}%"
        val countQueryStr = """
        SELECT COUNT(cmr) FROM  CancerMedicalRecord  cmr
        WHERE cmr.patientName like :keyword
        AND cmr.status in :status
    """
        val countQuery = entityManager.createQuery(countQueryStr, Long::class.java)
        countQuery.setParameter("keyword", likeKeyword)
        countQuery.setParameter("status", status)
        val result = countQuery.singleResult
        return result
    }

    override fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Status>,
        offset: Long
    ): List<CancerMedicalRecordDto> {
        val likeKeyword = "%${keyword?.trim()}%"
        val queryStr = """
        SELECT cmr FROM CancerMedicalRecord cmr
        WHERE cmr.patientName like :keyword
        AND cmr.status in (:status)
            ORDER BY cmr.id DESC
    """

        val query = entityManager.createQuery(queryStr, CancerMedicalRecord::class.java)
        query.setParameter("keyword", likeKeyword)
        query.setParameter("status", status)
        query.firstResult = offset.toInt()
        query.maxResults = Defaults.Pageable.DEFAULT_PAGE_SIZE
        val res = query.resultList
        return CancerMedicalRecordMapper.INSTANCE.cancerRecordsToDtos(res)
    }

    override fun findNextCancerMedical(department: String): Long {
        val countQueryStr = """
        SELECT COUNT(cmd) FROM CancerMedicalRecord cmd
        WHERE YEAR(cmd.saveYear) = :currentYear
        AND cmd.department = :department
        AND cmd.status = :status
    """

        val countQuery = entityManager.createQuery(countQueryStr, Long::class.java)
        countQuery.setParameter("currentYear", Instant.now().atZone(ZoneId.systemDefault()).year)
        countQuery.setParameter("department", department)
        countQuery.setParameter("status", Status.ACTIVATED)
        val result = countQuery.singleResult
        return result + 1
    }
}