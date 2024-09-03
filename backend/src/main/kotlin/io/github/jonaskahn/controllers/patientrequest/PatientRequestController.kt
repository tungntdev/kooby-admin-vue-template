package io.github.jonaskahn.controllers.patientrequest


import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Roles
import io.github.jonaskahn.middlewares.role.AccessVerifier
import io.github.jonaskahn.services.patientrequest.PatientRequestDto
import io.github.jonaskahn.services.patientrequest.PatientRequestService
import io.jooby.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Tag(name = "Patient Request", description = "APIs for managing patient requests")
@Path("secure")
class PatientRequestController @Inject constructor(
    private val patientRequestService: PatientRequestService,
    private val accessVerifier: AccessVerifier
) {

    @POST("/patientRequest/create")
    fun createNewRequest(request: PatientRequestForm) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.createRequest(request)
    }

    @PUT("/patientRequest/update")
    fun updatePatientRequest(request: PatientRequestForm) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.updateRequest(request)
    }

    @DELETE("/patientRequest/delete/{id}")
    fun deletePatientRequest(@PathParam("id") id: Int) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.deleteRequest(id)
    }

    @POST("/patientRequest/searchRecord")
    fun searchPatientRequest(request: SearchRequestForm): PageData<PatientRequestDto> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.search(request.keyword, request.states, request.pageNo)
    }
}