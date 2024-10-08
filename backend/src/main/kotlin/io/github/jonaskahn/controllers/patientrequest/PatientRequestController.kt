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

    @POST("/patient-request/create")
    fun createNewRequest(request: PatientRequestForm) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.createRequest(request)
    }

    @PUT("/patient-request/update")
    fun updatePatientRequest(request: PatientRequestForm) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.updateRequest(request)
    }

    @DELETE("/patient-request/delete/{id}")
    fun deletePatientRequest(@PathParam("id") id: Int) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.deleteRequest(id)
    }

    @POST("/patient-request/search")
    fun searchPatientRequest(request: SearchRequestForm): PageData<PatientRequestDto> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.search(request.keyword, request.states, request.pageNo, request.copyUser)
    }

    @GET("/patient-request/next-order")
    fun getNextPatientOrder(): Long {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.findNextPatientOrder()
    }

    @GET("/patient-request/next-delivery-order")
    fun getNextDeliveryOrder(): Long {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.findNextPatientDeliveryNumber()
    }

    @PUT("/patient-request/signed/{id}")
    fun signedPatientRequest(@PathParam("id") id: Int) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.setSigned(id)
    }

    @PUT("/patient-request/received/{id}")
    fun receivedPatientRequest(@PathParam("id") id: Int) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.setReceived(id)
    }

    @PUT("/patient-request/copied/{id}")
    fun progressedPatientRequest(@PathParam("id") id: Long) {
        accessVerifier.requireRole(Roles.COPY_MAN)
        return patientRequestService.setInProgress(id)
    }

    @PUT("/patient-request/delivered/{id}")
    fun deliveredPatientRequest(@PathParam("id") id: Int) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.setDelivered(id)
    }

    @PUT("/patient-request/assignment")
    fun assignedPatientRequest(assignment: AssignmentForm) {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.setAssignment(assignment.id, assignment.idCopyUser, assignment.appointmentDate)
    }

    @POST("/patient-request/patient-report")
    fun patientRequestReport(request: ReportForm): Collection<ReportResponse> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.patientRequestReport(request.startDate, request.endDate)
    }

    @POST("/patient-request/delivery-report")
    fun deliveryReport(request: ReportForm): Collection<ReportResponse> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.deliveryReport(request.startDate, request.endDate)
    }

    @POST("/patient-request/delivered-report")
    fun deliveredReport(request: ReportForm): Collection<ReportResponse> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return patientRequestService.deliveredReport(request.startDate, request.endDate)
    }
}