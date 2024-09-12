package io.github.jonaskahn.services.storage

import io.github.jonaskahn.controllers.storage.CancerMedicalRecordRequest
import io.github.jonaskahn.entities.CancerMedicalRecord
import io.github.jonaskahn.entities.enums.Status
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper
interface CancerMedicalRecordMapper {
    companion object {
        var INSTANCE: CancerMedicalRecordMapper = Mappers.getMapper(CancerMedicalRecordMapper::class.java)
    }
    
    fun cancerRecordRequestToCancerRecord(cancer: CancerMedicalRecordRequest): CancerMedicalRecord

    fun cancerRecordToDto(cancer: CancerMedicalRecord): CancerMedicalRecordDto

    fun cancerRecordsToDtos(cancerList: List<CancerMedicalRecord>): List<CancerMedicalRecordDto>

}