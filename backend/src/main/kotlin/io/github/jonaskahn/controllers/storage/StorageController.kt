package io.github.jonaskahn.controllers.storage

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Roles
import io.github.jonaskahn.middlewares.role.AccessVerifier
import io.github.jonaskahn.services.storage.CancerMedicalRecordDto
import io.github.jonaskahn.services.storage.StorageService
import io.jooby.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Tag(name = "Storage", description = "APIs for managing cancer medical record")
@Path("secure")
class StorageController @Inject constructor(
    private val storageService: StorageService,
    private val accessVerifier: AccessVerifier
) {
    @POST("/storage/create")
    fun createStorage(request: CancerMedicalRecordRequest) {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.create(request)
    }

    @PUT("/storage/update/{id}")
    fun updateStorage(@PathParam("id") id: Int, request: CancerMedicalRecordRequest) {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.update(id, request)
    }

    @POST("/storage/search")
    fun searchStorage(request: SearchStorageForm): PageData<CancerMedicalRecordDto> {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.search(request.keyword, request.department, request.statuses, request.pageNo)
    }

    @DELETE("/storage/delete/{id}")
    fun deleteStorage(@PathParam("id") id: Long) {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.delete(id)
    }

    @POST("/storage/find-next-save-number")
    fun findNextSaveNumber(request: DepartmentRequest): Long {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.findNextSaveNumber(request.department ?: "")
    }
}