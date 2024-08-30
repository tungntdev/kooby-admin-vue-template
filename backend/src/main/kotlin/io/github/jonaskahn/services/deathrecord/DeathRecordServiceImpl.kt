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
        return repository.create(record)
    }

    override fun update(record: DeathRecordForm) {
        return repository.update(record)
    }

    override fun delete(recordId: Int) {
        return repository.delete(recordId)
    }

    override fun search(keyword: String?, statuses: Collection<Status>, pageNo: Long): PageData<DeathRecord> {
        return super.search(
            statuses,
            listOf(),
            pageNo,
            {status, _ -> repository.countByKeywordAndState(keyword, status)},
            {status, _, offset -> repository.searchByKeywordAndStateAndOffset(keyword, status, offset)}
        )
    }
}