package io.github.jonaskahn.repositories.impl

import io.github.jonaskahn.constants.Defaults
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.middlewares.context.UserContextHolder
import io.github.jonaskahn.repositories.AbstractBaseRepository
import io.github.jonaskahn.repositories.DeathRecordRepository
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import java.time.Instant
import java.time.ZoneId

class DeathRecordRepositoryImpl @Inject constructor(
    override val entityManager: EntityManager
) : AbstractBaseRepository(entityManager), DeathRecordRepository {
    override fun create(deathRecord: DeathRecord) {
        deathRecord.receiver = UserContextHolder.getCurrentUserId()
        deathRecord.createdBy = UserContextHolder.getCurrentUserId()
        deathRecord.createdAt = Instant.now()
        deathRecord.status = Status.ACTIVATED
        entityManager.persist(deathRecord)
    }

    override fun update(deathRecord: DeathRecord) {
        deathRecord.updatedBy = UserContextHolder.getCurrentUserId()
        deathRecord.updatedAt = Instant.now()
        entityManager.merge(deathRecord)
    }

    override fun delete(id: Int) {
        val entity = entityManager.find(DeathRecord::class.java, id)
        if (entity != null) {
            entity.status = Status.DELETED
            entity.updatedBy = UserContextHolder.getCurrentUserId()
            entity.updatedAt = Instant.now()
            entityManager.merge(entity)
        }
    }

    override fun countByKeywordAndStatus(
        keyword: String?,
        status: Collection<Status>
    ): Long {
        val likeKeyword = "%${keyword?.trim()}%"
        val countQueryStr = """
        SELECT COUNT(dr) FROM DeathRecord dr
        WHERE dr.patientName like :keyword
        AND dr.status in :status
    """

        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("keyword", likeKeyword)
        countQuery.setParameter("status", status)
        val result = countQuery.singleResult
        return result
    }

    override fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Status>,
        offset: Long
    ): Collection<DeathRecord> {
        val likeKeyword = "%${keyword?.trim()}%"
        val queryStr = """
        SELECT dr FROM DeathRecord dr
        WHERE dr.patientName LIKE :keyword
            AND dr.status in :status
            ORDER BY dr.id DESC
    """

        val query = entityManager.createQuery(queryStr, DeathRecord::class.java)
        query.setParameter("keyword", likeKeyword)
        query.setParameter("status", status)
        query.firstResult = offset.toInt()
        query.maxResults = Defaults.Pageable.DEFAULT_PAGE_SIZE
        return query.resultList
    }

    override fun findById(id: Int): DeathRecord {
        return entityManager.find(DeathRecord::class.java, id)
    }

    override fun findNextDeathNumber(): Long {
        val countQueryStr = """
        SELECT COUNT(dr) FROM DeathRecord dr
        WHERE YEAR(dr.receptionDate) = :currentYear
        AND dr.status = :status
    """

        val countQuery = entityManager.createQuery(countQueryStr, Long::class.javaObjectType)
        countQuery.setParameter("currentYear", Instant.now().atZone(ZoneId.systemDefault()).year)
        countQuery.setParameter("status", Status.ACTIVATED)
        val result = countQuery.singleResult
        return result + 1
    }

}