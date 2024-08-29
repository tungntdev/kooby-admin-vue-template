package io.github.jonaskahn.services.deathrecord

import com.google.inject.ImplementedBy
import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status

@ImplementedBy(DeathRecordServiceImpl::class)
interface DeathRecordService {
    fun create(record: DeathRecord)

    fun update(record: DeathRecord)
    fun delete(recordId: Int)

    fun search(
        keyword: String? = null,
        statuses: Collection<Status> = listOf(Status.ACTIVATED),
        pageNo: Long = 0L,
    ): PageData<DeathRecord>

}