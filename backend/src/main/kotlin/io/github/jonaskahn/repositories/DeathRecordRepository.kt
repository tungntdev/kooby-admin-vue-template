package io.github.jonaskahn.repositories

import io.github.jonaskahn.entities.DeathRecord

interface DeathRecordRepository {
    fun create(deathRecord: DeathRecord)
    fun update(deathRecord: DeathRecord)
    fun delete(id: Int)
    fun countByKeywordAndState(keyword: String?, status: Collection<Int>): Long
    fun searchByKeywordAndStateAndOffset(keyword: String?, state: Collection<Int>, offset: Long) : Collection<DeathRecord>
}