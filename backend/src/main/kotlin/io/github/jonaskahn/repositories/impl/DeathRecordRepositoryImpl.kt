package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.PatientRequest
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.DeathRecordRepository
import jakarta.inject.Inject
import jakarta.persistence.EntityManager

class DeathRecordRepositoryImpl @Inject constructor(
    override val entityManager: EntityManager
): AbstractBaseRepository(entityManager), DeathRecordRepository {
    override fun create(deathRecord: DeathRecord) {
        entityManager.persist(deathRecord)
    }

    override fun update(deathRecord: DeathRecord) {
        entityManager.merge(deathRecord)
    }

    override fun delete(id: Int) {
        val entity = entityManager.find(PatientRequest::class.java, id)
        if (entity != null) {
            entity.status = Status.DELETED
            entityManager.merge(entity)
        }
    }

    override fun countByKeywordAndState(keyword: String?, statuses: Collection<Int>): Long {
        val likeKeyword = "${keyword?.trim()}%"

        val countQueryStr = """
        SELECT COUNT(dr) FROM DeathRecord dr
        WHERE (dr.patientName LIKE :keyword 
            OR (CAST(:keywordInt AS integer) IS NOT NULL AND dr.deathNumber = CAST(:keywordInt AS integer)) 
            OR (CAST(:keywordInt AS integer) IS NOT NULL AND dr.patientNumber = CAST(:keywordInt AS integer))
            )
            AND dr.status IN :statuses
    """

        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("keyword", likeKeyword)
        val keywordInt: Int? = keyword?.trim()?.toIntOrNull()
        countQuery.setParameter("keywordInt", keywordInt)
        countQuery.setParameter("statuses", statuses)
        return countQuery.singleResult
    }


    override fun searchByKeywordAndStateAndOffset(
        keyword: String?,
        state: Collection<Int>,
        offset: Long
    ): Collection<DeathRecord> {
        TODO("Not yet implemented")
    }

}