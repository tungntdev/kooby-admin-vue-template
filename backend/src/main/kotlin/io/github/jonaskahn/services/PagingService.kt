package io.github.jonaskahn.services

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Defaults.Pageable.DEFAULT_PAGE_SIZE
import io.github.jonaskahn.entities.enums.State
import io.github.jonaskahn.entities.enums.Status
import kotlin.math.ceil

abstract class PagingService {

    fun <T> search(
        statuses: Collection<Status> = listOf(),
        states: Collection<State> = listOf(),
        pageNo: Long,
        count: (statuses: Collection<Status>, states: Collection<State>) -> Long,
        lookup: (statuses: Collection<Status>, states: Collection<State>, offset: Long) -> Collection<T>
    ): PageData<T> {
        // Sử dụng trực tiếp statuses và states mà không chuyển đổi thành id
        val effectiveStatuses = statuses.ifEmpty { Status.entries }
        val effectiveStates = states.ifEmpty { State.entries }

        // Tính tổng số mục dựa trên trực tiếp các Status và State
        val totalItems = count(effectiveStatuses, effectiveStates)

        // Tính toán tổng số trang
        val totalPages = (ceil(totalItems * 1F / DEFAULT_PAGE_SIZE)).toLong()

        // Tính toán số trang hợp lệ
        val calculatedPageNumber = calculatePageNumber(pageNo, totalPages)

        // Tính toán offset cho việc truy vấn dữ liệu
        val offset = (calculatedPageNumber - 1) * DEFAULT_PAGE_SIZE

        // Lấy dữ liệu từ lookup
        val records = if (totalItems > 0) lookup(effectiveStatuses, effectiveStates, offset) else listOf()

        // Trả về đối tượng PageData với dữ liệu phân trang
        return PageData(
            data = records,
            currentPage = if (totalPages == 0L) 0L else calculatedPageNumber,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }


    private fun calculatePageNumber(pageNumber: Long, totalPages: Long): Long {
        return if (totalPages < 1) {
            1L
        } else if (pageNumber <= 1) {
            1L
        } else if (pageNumber >= totalPages) {
            totalPages
        } else {
            pageNumber
        }
    }
}