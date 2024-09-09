package io.github.jonaskahn.services.storage

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.storage.CancerMedicalRecordRequest
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.repositories.CancerMedicalRecordRepository
import io.github.jonaskahn.services.PagingService
import jakarta.inject.Inject

internal class StorageServiceImpl @Inject constructor(
    private val cancerMedicalRecordRepository: CancerMedicalRecordRepository
) : StorageService, PagingService() {
    override fun create(record: CancerMedicalRecordRequest) {
        val newRecord = CancerMedicalRecordMapper.INSTANCE.cancerRecordRequestToCancerRecord(record)
        cancerMedicalRecordRepository.create(newRecord)
    }

    override fun update(id: Int, record: CancerMedicalRecordRequest) {
        val newRecord = CancerMedicalRecordMapper.INSTANCE.cancerRecordRequestToCancerRecord(record)
        newRecord.id = id
        cancerMedicalRecordRepository.update(newRecord)
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun search(
        keyword: String?,
        statuses: Collection<Status>,
        pageNo: Long
    ): PageData<CancerMedicalRecordDto> {
        TODO("Not yet implemented")
    }
}