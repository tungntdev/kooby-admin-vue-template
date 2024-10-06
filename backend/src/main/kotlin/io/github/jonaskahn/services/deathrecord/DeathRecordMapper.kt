package io.github.jonaskahn.services.deathrecord

import io.github.jonaskahn.controllers.deathrecord.DeathRecordForm
import io.github.jonaskahn.entities.DeathRecord
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.factory.Mappers

@Mapper
interface DeathRecordMapper {
    companion object {
        val INSTANCE: DeathRecordMapper = Mappers.getMapper(DeathRecordMapper::class.java)
    }

    fun toDeathRecord(record: DeathRecordForm): DeathRecord

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun fromRecordFormToDeathRecord(recordForm: DeathRecordForm, @MappingTarget existingRecord: DeathRecord)
}