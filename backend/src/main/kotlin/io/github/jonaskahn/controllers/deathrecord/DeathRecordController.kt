package io.github.jonaskahn.controllers.deathrecord

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Roles
import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.middlewares.role.AccessVerifier
import io.github.jonaskahn.services.deathrecord.DeathRecordService
import io.jooby.annotation.DELETE
import io.jooby.annotation.GET
import io.jooby.annotation.POST
import io.jooby.annotation.Path
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Tag(name = "Death Record", description = "APIs for Death Record management")
@Path("/secure")
class DeathRecordController @Inject constructor(
    private val service: DeathRecordService,
    private val accessVerifier: AccessVerifier
) {

    @POST("/death-records/create")
    fun addDeathRecord(deathRecord: DeathRecordForm) {
        accessVerifier.requireRole(Roles.DOCUMENT_ISSUER)
        return service.create(deathRecord)
    }

    @POST("/death-records/update")
    fun updateDeathRecord(deathRecord: DeathRecordForm) {
        accessVerifier.requireRole(Roles.DOCUMENT_ISSUER)
        return service.update(deathRecord)
    }

    @DELETE("/death-records/delete/{deathRecordId}")
    fun delete(deathRecordId: Int) {
        accessVerifier.requireRole(Roles.DOCUMENT_ISSUER)
        return service.delete(deathRecordId)
    }

    @POST("/death-records/search")
    fun search(request: SearchForm) : PageData<DeathRecord> {
        accessVerifier.requireRole(Roles.DOCUMENT_ISSUER)
        return service.search(request.keyword, request.status, request.pageNo)
    }

}