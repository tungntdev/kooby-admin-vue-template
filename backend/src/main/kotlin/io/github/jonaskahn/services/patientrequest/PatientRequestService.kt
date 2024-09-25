package io.github.jonaskahn.services.patientrequest

import com.google.inject.ImplementedBy
import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.controllers.patientrequest.PatientRequestForm
import io.github.jonaskahn.controllers.patientrequest.ReportResponse
import io.github.jonaskahn.entities.enums.State
import java.time.LocalDate

@ImplementedBy(PatientRequestServiceImpl::class)
interface PatientRequestService {
    fun createRequest(request: PatientRequestForm)

    fun updateRequest(request: PatientRequestForm)

    fun deleteRequest(requestId: Int)

    fun search(
        keyword: String? = null,
        states: Collection<State> = listOf(State.PENDING),
        pageNo: Long = 0L,
        copyUser: Int? = 0,
    ): PageData<PatientRequestDto>

    fun findNextPatientOrder(): Long
    fun findNextPatientDeliveryNumber(): Long

    fun setSigned(id: Int)

    fun setReceived(id: Int)

    fun setInProgress(id: Long)

    fun setDelivered(id: Int)

    fun setAssignment(id: Int, copierId: Int, appointmentDate: LocalDate)

    fun patientRequestReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>

    fun deliveryReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>

    fun deliveredReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>

}