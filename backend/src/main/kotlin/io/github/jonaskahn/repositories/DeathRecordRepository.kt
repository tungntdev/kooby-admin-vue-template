package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.controllers.deathrecord.DeathRecordForm
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.repositories.impl.DeathRecordRepositoryImpl

@ImplementedBy(DeathRecordRepositoryImpl::class)
interface DeathRecordRepository {
    fun create(deathRecord: DeathRecordForm)
    fun update(deathRecord: DeathRecordForm)
    fun delete(id: Int)
    fun countByKeywordAndStatus(keyword: String?, status: Collection<Int>): Long
    fun searchByKeywordAndStatusAndOffset(keyword: String?, status: Collection<Int>, offset: Long) : Collection<DeathRecord>
}