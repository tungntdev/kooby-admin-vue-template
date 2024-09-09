package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.entities.CancerMedicalRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.middlewares.context.UserContextHolder
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.CancerMedicalRecordRepository
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import java.time.Instant

class CancerMedicalRecordRepositoryImpl @Inject constructor(
    override val entityManager: EntityManager
) : AbstractBaseRepository(entityManager), CancerMedicalRecordRepository {
    override fun create(cancerMedicalRecord: CancerMedicalRecord) {
        cancerMedicalRecord.createdAt = Instant.now()
        cancerMedicalRecord.createdBy = UserContextHolder.getCurrentUserId()
        cancerMedicalRecord.status = Status.ACTIVATED

        entityManager.persist(cancerMedicalRecord)
    }

    override fun update(cancerMedicalRecord: CancerMedicalRecord) {
        val exitingRecord = entityManager.find(CancerMedicalRecord::class.java, cancerMedicalRecord.id)


        cancerMedicalRecord.updatedAt = Instant.now()
        cancerMedicalRecord.updatedBy = UserContextHolder.getCurrentUserId()

        cancerMedicalRecord.status = exitingRecord.status
        cancerMedicalRecord.createdAt = exitingRecord.createdAt
        cancerMedicalRecord.createdBy = exitingRecord.createdBy

        entityManager.merge(cancerMedicalRecord)
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun countByKeywordAndStatus(keyword: String?, status: Collection<Int>): Long {
        TODO("Not yet implemented")
    }

    override fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Int>,
        offset: Long
    ): Collection<CancerMedicalRecord> {
        TODO("Not yet implemented")
    }

    override fun findNextCancerMedical(): Long {
        TODO("Not yet implemented")
    }
}