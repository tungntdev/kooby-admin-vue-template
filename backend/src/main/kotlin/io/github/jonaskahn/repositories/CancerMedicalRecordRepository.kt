package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.entities.CancerMedicalRecord
import io.github.jonaskahn.repositories.impl.CancerMedicalRecordRepositoryImpl

@ImplementedBy(CancerMedicalRecordRepositoryImpl::class)
interface CancerMedicalRecordRepository {
    fun create(cancerMedicalRecord: CancerMedicalRecord)
    fun update(cancerMedicalRecord: CancerMedicalRecord)
    fun delete(id: Long)
    fun countByKeywordAndStatus(keyword: String?, status: Collection<Int>): Long
    fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Int>,
        offset: Long
    ): Collection<CancerMedicalRecord>

    fun findNextCancerMedical(): Long

}