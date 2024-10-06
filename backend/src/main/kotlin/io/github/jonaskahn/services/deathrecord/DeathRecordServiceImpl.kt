package io.github.jonaskahn.services.deathrecord

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.deathrecord.DeathRecordForm
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.repositories.DeathRecordRepository
import io.github.jonaskahn.services.PagingService
import jakarta.inject.Inject

class DeathRecordServiceImpl @Inject constructor(
    private val repository: DeathRecordRepository
) : DeathRecordService, PagingService() {
    override fun create(record: DeathRecordForm) {
        val newRecord = DeathRecordMapper.INSTANCE.toDeathRecord(record)
        return repository.create(newRecord)
    }

    override fun update(record: DeathRecordForm) {
        val existingRecord = repository.findById(record.id!!)
        DeathRecordMapper.INSTANCE.fromRecordFormToDeathRecord(record, existingRecord)

        return repository.update(existingRecord)
    }

    override fun delete(recordId: Int) {
        return repository.delete(recordId)
    }

    override fun searchRecord(
        keyword: String?,
        statuses: Collection<Status>,
        pageNo: Long
    ): PageData<DeathRecord> {
        return super.search(
            statuses,
            listOf(),
            pageNo,
            { statuses, _ -> repository.countByKeywordAndStatus(keyword, statuses) },
            { statuses, _, offset -> repository.searchByKeywordAndStatusAndOffset(keyword, statuses, offset) }
        )
    }

    override fun findNextDeathNumber(): Long {
        return repository.findNextDeathNumber()
    }
}