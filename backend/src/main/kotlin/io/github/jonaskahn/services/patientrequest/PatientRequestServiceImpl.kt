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
    private val repository: PatientRequestRepository,
) : PatientRequestService, PagingService() {
    override fun createRequest(request: PatientRequestForm) {
        val newRequest = PatientRequestEntityToDtoMapper.INSTANCE.formToPatientRequest(request)
        repository.create(newRequest)
    }

    override fun updateRequest(request: PatientRequestForm) {
        val exitingPatient = request.id?.let { repository.findById(it) }

        if (exitingPatient != null) {
            PatientRequestEntityToDtoMapper.INSTANCE.formTargetPatientRequest(request, exitingPatient)
        }
        if (exitingPatient != null) {
            repository.update(exitingPatient)
        }
    }

    override fun deleteRequest(requestId: Int) {
        return repository.delete(requestId)
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
            { _, states -> repository.countByKeywordAndState(keyword, states, copyUser) },
            { _, states, offset ->
                repository.searchByKeywordAndStateAndOffset(
                    keyword,
                    states,
                    offset,
                    copyUser
                )
            }

        )
    }

    override fun findNextPatientOrder(): Long {
        return repository.findNextPatientOrder()
    }

    override fun findNextPatientDeliveryNumber(): Long {
        return repository.findNextPatientDeliveryNumber()
    }

    override fun setSigned(id: Int) {
        return repository.setSigned(id)
    }

    override fun setReceived(id: Int) {
        return repository.setReceived(id)
    }

    override fun setInProgress(id: Long) {
        return repository.setInProgress(id)
    }

    override fun setDelivered(id: Int) {
        return repository.setDelivered(id)
    }

    override fun setAssignment(id: Int, copierId: Int, appointmentDate: LocalDate) {
        return repository.setAssignment(id, copierId, appointmentDate)
    }

    override fun patientRequestReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return repository.patientRequestReport(startDate, endDate)
    }

    override fun deliveryReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return repository.deliveryReport(startDate, endDate)
    }

    override fun deliveredReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse> {
        return repository.deliveredReport(startDate, endDate)
    }
}