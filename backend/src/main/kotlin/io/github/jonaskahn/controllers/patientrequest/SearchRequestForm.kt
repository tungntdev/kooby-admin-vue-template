package io.github.jonaskahn.controllers.patientrequest

import io.github.jonaskahn.entities.enums.State
import java.time.LocalDate

data class SearchRequestForm(
    val keyword: String? = null,
    val states: Collection<State> = listOf(State.PENDING),
    val pageNo: Long = 0L,
    val copyUser: Int?
)

data class AssignmentForm(
    val id: Int,
    val idCopyUser: Int,
    val appointmentDate: LocalDate,
)

data class ReportForm(
    val startDate: LocalDate,
    val endDate: LocalDate,
)