package io.github.jonaskahn.controllers.deathrecord

import io.github.jonaskahn.entities.DeathRecord
import io.github.jonaskahn.services.deathrecord.DeathRecordService
import io.jooby.annotation.GET
import io.jooby.annotation.POST
import io.jooby.annotation.Path
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Tag(name = "Death Record", description = "APIs for Death Record management")
@Path("/secure")
class DeathRecordController @Inject constructor(private val service: DeathRecordService) {

    @POST("/death-records/create")
    fun addDeathRecord(deathRecord: DeathRecord) {
        return service.create(deathRecord)
    }
}