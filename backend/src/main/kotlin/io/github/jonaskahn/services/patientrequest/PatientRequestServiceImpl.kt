package io.github.jonaskahn.services.patientrequest

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.patientrequest.PatientRequestForm
import io.github.jonaskahn.controllers.patientrequest.ReportResponse
import io.github.jonaskahn.entities.enums.State
import io.github.jonaskahn.repositories.PatientRequestRepository
import io.github.jonaskahn.services.PagingService
import jakarta.inject.Inject
import java.time.LocalDate

internal class PatientRequestServiceImpl @Inject constructor(
    private val patientRequestRepository: PatientRequestRepository,
) : PatientRequestService, PagingService() {
    override fun createRequest(request: PatientRequestForm) {
        val newRequest = PatientRequestEntityToDtoMapper.INSTANCE.formToPatientRequest(request)
        patientRequestRepository.create(newRequest)
    }

    override fun updateRequest(request: PatientRequestForm) {
        val existingRequest = PatientRequestEntityToDtoMapper.INSTANCE.formToPatientRequest(request)
        patientRequestRepository.update(existingRequest)
    }


    override fun deleteRequest(requestId: Int) {
        return patientRequestRepository.delete(requestId)
    }

    override fun search(
        keyword: String?,
        states: Collection<State>,
        pageNo: Long,
        copyUser: Int?
    ): PageData<PatientRequestDto> {
        return super.search(
            listOf(),
            states,
            pageNo,
            { _, states -> patientRequestRepository.countByKeywordAndState(keyword, states, copyUser) },
            { _, states, offset ->
                patientRequestRepository.searchByKeywordAndStateAndOffset(
                    keyword,
                    states,
                    offset,
                    copyUser
                )
            }

        )
    }

    override fun findNextPatientOrder(): Long {
        return patientRequestRepository.findNextPatientOrder()
    }

    override fun findNextPatientDeliveryNumber(): Long {
        return patientRequestRepository.findNextPatientDeliveryNumber()
    }

    override fun setSigned(id: Int) {
        return patientRequestRepository.setSigned(id)
    }

    override fun setReceived(id: Int) {
        return patientRequestRepository.setReceived(id)
    }

    override fun setInProgress(id: Long) {
        return patientRequestRepository.setInProgress(id)
    }

    override fun setDelivered(id: Int) {
        return patientRequestRepository.setDelivered(id)
    }

    override fun setAssignment(id: Int, copierId: Int, appointmentDate: LocalDate) {
        return patientRequestRepository.setAssignment(id, copierId, appointmentDate)
    }

    override fun patientRequestReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return patientRequestRepository.patientRequestReport(startDate, endDate)
    }

    override fun deliveryReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return patientRequestRepository.deliveryReport(startDate, endDate)
    }

    override fun deliveredReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return patientRequestRepository.deliveredReport(startDate, endDate)
    }
}