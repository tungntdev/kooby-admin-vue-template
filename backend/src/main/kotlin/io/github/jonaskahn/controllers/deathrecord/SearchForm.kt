package io.github.jonaskahn.controllers.deathrecord
import io.github.jonaskahn.entities.enums.Status

data class SearchForm(
    val keyword: String? = null,
    val status: Collection<Status> = listOf(Status.ACTIVATED),
    val pageNo: Long =0L
)