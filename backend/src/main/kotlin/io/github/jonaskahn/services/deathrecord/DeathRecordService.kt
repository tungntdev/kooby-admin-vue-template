package io.github.jonaskahn.services.deathrecord

import com.google.inject.ImplementedBy
import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.deathrecord.DeathRecordForm
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status

@ImplementedBy(DeathRecordServiceImpl::class)
interface DeathRecordService {
    fun create(record: DeathRecordForm)
    fun update(record: DeathRecordForm)
    fun delete(recordId: Int)

    fun searchRecord(
        keyword: String? = null,
        statuses: Collection<Status> = listOf(Status.ACTIVATED),
        pageNo: Long = 0L,
    ): PageData<DeathRecord>

}