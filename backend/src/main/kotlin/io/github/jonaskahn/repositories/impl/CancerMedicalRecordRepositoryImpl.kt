package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.constants.Defaults
import io.github.jonaskahn.entities.CancerMedicalRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.middlewares.context.UserContextHolder
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.CancerMedicalRecordRepository
import io.github.jonaskahn.services.storage.CancerMedicalRecordDto
import io.github.jonaskahn.services.storage.CancerMedicalRecordMapper
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import java.time.Instant

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

//        cancerMedicalRecord.updatedAt = Instant.now()
//        cancerMedicalRecord.updatedBy = UserContextHolder.getCurrentUserId()
        cancerMedicalRecord.status = exitingRecord.status
        cancerMedicalRecord.createdAt = exitingRecord.createdAt
        cancerMedicalRecord.createdBy = exitingRecord.createdBy

        entityManager.merge(cancerMedicalRecord)
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun countByKeywordAndStatus(keyword: String?, status: Collection<Int>): Long {
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
        status: Collection<Int>,
        offset: Long
    ): Collection<CancerMedicalRecordDto> {
        val likeKeyword = "%${keyword?.trim()}%"
        val queryStr = """
        SELECT cmr FROM CancerMedicalRecord cmr
        WHERE cmr.patientName like :keyword
        AND cmr.status in :status
            ORDER BY cmr.id DESC
    """

        val query = entityManager.createQuery(queryStr, CancerMedicalRecord::class.java)
        query.setParameter("keyword", likeKeyword)
        query.setParameter("status", status)
        query.firstResult = offset.toInt()
        query.maxResults = Defaults.Pageable.DEFAULT_PAGE_SIZE
        return  CancerMedicalRecordMapper.INSTANCE.cancerRecordsToDtos(query.resultList)
    }

    override fun findNextCancerMedical(): Long {
        TODO("Not yet implemented")
    }
}