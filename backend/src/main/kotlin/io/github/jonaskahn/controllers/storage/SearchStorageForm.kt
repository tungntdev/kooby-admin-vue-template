package io.github.jonaskahn.controllers.storage

import io.github.jonaskahn.entities.enums.Status

data class SearchStorageForm(
    val keyword: String? = null,
    val department: String? = null,
    val statuses: Collection<Status> = listOf(Status.ACTIVATED),
    val pageNo: Long = 0L
)

data class DepartmentRequest(val department: String? = null)
