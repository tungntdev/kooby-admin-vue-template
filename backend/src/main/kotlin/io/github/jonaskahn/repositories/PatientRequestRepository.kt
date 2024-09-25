package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.controllers.patientrequest.ReportResponse
import io.github.jonaskahn.entities.PatientRequest
import io.github.jonaskahn.entities.enums.State
import io.github.jonaskahn.repositories.impl.PatientRequestRepositoryImpl
import io.github.jonaskahn.services.patientrequest.PatientRequestDto
import java.time.LocalDate

@ImplementedBy(PatientRequestRepositoryImpl::class)
interface PatientRequestRepository {
    fun create(entity: PatientRequest)

    fun update(entity: PatientRequest)

    fun delete(id: Int)

    fun countByKeywordAndState(keyword: String?, state: Collection<State>): Long

    fun searchByKeywordAndStateAndOffset(
        keyword: String?,
        state: Collection<State>,
        offset: Long
    ): Collection<PatientRequestDto>


    fun findNextPatientOrder(): Long

    fun findNextPatientDeliveryNumber(): Long

    fun setSigned(id: Int)

    fun setReceived(id: Int)

    fun setInProgress(id: Long)

    fun setDelivered(id: Int)

    fun setAssignment(id: Int, idCopyUser: Int, appointmentDate: LocalDate)

    fun patientRequestReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>

    fun deliveryReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>

    fun deliveredReport(startDate: LocalDate?, endDate: LocalDate?): Collection<ReportResponse>


}