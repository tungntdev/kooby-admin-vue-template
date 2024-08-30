package io.github.jonaskahn.repositories

import io.github.jonaskahn.controllers.deathrecord.DeathRecordForm
import io.github.jonaskahn.entities.DeathRecord

interface DeathRecordRepository {
    fun create(deathRecord: DeathRecordForm)
    fun update(deathRecord: DeathRecordForm)
    fun delete(id: Int)
    fun countByKeywordAndState(keyword: String?, status: Collection<Int>): Long
    fun searchByKeywordAndStateAndOffset(keyword: String?, status: Collection<Int>, offset: Long) : Collection<DeathRecord>
}