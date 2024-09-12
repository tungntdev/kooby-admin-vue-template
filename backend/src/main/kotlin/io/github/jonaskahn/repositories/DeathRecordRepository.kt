package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.repositories.impl.DeathRecordRepositoryImpl

@ImplementedBy(DeathRecordRepositoryImpl::class)
interface DeathRecordRepository {
    fun create(deathRecord: DeathRecord)
    fun update(deathRecord: DeathRecord)
    fun delete(id: Int)
    fun countByKeywordAndStatus(keyword: String?, status: Collection<Status>): Long
    fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Status>,
        offset: Long
    ): Collection<DeathRecord>

    fun findById(id: Int): DeathRecord
    fun findNextDeathNumber(): Long
}