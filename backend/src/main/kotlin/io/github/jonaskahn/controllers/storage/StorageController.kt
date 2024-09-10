package io.github.jonaskahn.controllers.storage

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Roles
import io.github.jonaskahn.middlewares.role.AccessVerifier
import io.github.jonaskahn.services.storage.CancerMedicalRecordDto
import io.github.jonaskahn.services.storage.StorageService
import io.jooby.annotation.POST
import io.jooby.annotation.Path
import io.jooby.annotation.PathParam
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

    @POST("/storage/update/{id}")
    fun updateStorage(@PathParam("id") id: Int, request: CancerMedicalRecordRequest) {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.update(id, request)
    }

    @POST("/storage/search")
    fun searchStorage(request: SearchStorageForm): PageData<CancerMedicalRecordDto> {
        accessVerifier.requireRole((Roles.MANAGER))
        return storageService.search(request.keyword, request.statuses, request.pageNo)
    }
}