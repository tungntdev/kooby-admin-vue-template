package io.github.jonaskahn.services.patientrequest

import io.github.jonaskahn.controllers.patientrequest.PatientRequestForm
import io.github.jonaskahn.controllers.patientrequest.ReportResponse
import io.github.jonaskahn.entities.Assignment
import io.github.jonaskahn.entities.PatientRequest
import io.github.jonaskahn.entities.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
interface PatientRequestEntityToDtoMapper {
    companion object {
        var INSTANCE: PatientRequestEntityToDtoMapper = Mappers.getMapper(PatientRequestEntityToDtoMapper::class.java)
    }

    @Mappings(
        Mapping(source = "assignment.id", target = "idAssignment"),
        Mapping(source = "patientRequest.status", target = "requestStatus"),
        Mapping(source = "patientRequest.id", target = "id"),
        Mapping(source = "patientRequest.createdAt", target = "createdAt"),
        Mapping(source = "patientRequest.createdBy", target = "createdBy"),
    )
    fun toDto(patientRequest: PatientRequest, assignment: Assignment?, user: User?): PatientRequestDto

    fun formToPatientRequest(formPatient: PatientRequestForm): PatientRequest

    fun toReportResponse(patientRequest: Collection<PatientRequest>): Collection<ReportResponse>
}