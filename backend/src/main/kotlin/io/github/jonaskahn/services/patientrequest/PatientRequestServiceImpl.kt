package io.github.jonaskahn.services.patientrequest

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.patientrequest.PatientRequestForm
import io.github.jonaskahn.entities.enums.State
import io.github.jonaskahn.repositories.PatientRequestRepository
import io.github.jonaskahn.services.PagingService
import jakarta.inject.Inject

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
        TODO("Not yet implemented")
    }

    override fun search(
        keyword: String?,
        states: Collection<State>,
        pageNo: Long
    ): PageData<PatientRequestDto> {
        return super.search(
            listOf(),
            states,
            pageNo,
            { _, states -> patientRequestRepository.countByKeywordAndState(keyword, states) },
            { _, states, offset -> patientRequestRepository.searchByKeywordAndStateAndOffset(keyword, states, offset) }

        )
    }

    override fun findNextPatientOrder(): Long {
        return patientRequestRepository.findNextPatientOrder()
    }

    override fun findNextPatientDeliveryNumber(): Long {
        return patientRequestRepository.findNextPatientDeliveryNumber()
    }
}