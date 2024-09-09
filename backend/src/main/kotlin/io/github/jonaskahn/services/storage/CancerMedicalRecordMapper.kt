package io.github.jonaskahn.services.storage

import io.github.jonaskahn.controllers.storage.CancerMedicalRecordRequest
import io.github.jonaskahn.entities.CancerMedicalRecord
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface CancerMedicalRecordMapper {
    companion object {
        var INSTANCE: CancerMedicalRecordMapper = Mappers.getMapper(CancerMedicalRecordMapper::class.java)
    }
    
    fun cancerRecordRequestToCancerRecord(cancer: CancerMedicalRecordRequest): CancerMedicalRecord
}