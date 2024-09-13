package io.github.jonaskahn.services.storage

import com.google.inject.ImplementedBy
import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.storage.CancerMedicalRecordRequest
import io.github.jonaskahn.entities.enums.Status

@ImplementedBy(StorageServiceImpl::class)
interface StorageService {
    fun create(record: CancerMedicalRecordRequest)
    fun update(id: Int, record: CancerMedicalRecordRequest)
    fun delete(id: Long)

    fun search(
        keyword: String? = null,
        department: String? = null,
        statuses: Collection<Status> = listOf(Status.ACTIVATED),
        pageNo: Long = 0L,
    ): PageData<CancerMedicalRecordDto>

    fun findNextSaveNumber(department: String): Long
}