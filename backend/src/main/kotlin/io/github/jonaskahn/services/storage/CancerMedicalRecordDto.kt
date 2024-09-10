package io.github.jonaskahn.services.storage

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.jonaskahn.assistant.jackson.MessageJsonSerializer
import io.github.jonaskahn.entities.enums.Status
import jakarta.persistence.Table
import java.io.Serializable
import java.time.Instant

open class CancerMedicalRecordDto: Serializable {
    open var id: Int? = null

    open var department: String? = null

    @JsonAlias("save_number")
    open var saveNumber: Int? = null

    @JsonAlias("save_year")
    open var saveYear: Int? = null

    @JsonAlias("patient_number")
    open var patientNumber: Int? = null

    @JsonAlias("patient_name")
    open var patientName: String? = null

    open var sex: String? = null

    open var birthday: String? = null

    open var type: String? = null

    open var address: String? = null

    @JsonAlias("in_date")
    open var inDate: Instant? = null

    @JsonAlias("out_date")
    open var outDate: Instant? = null
    open var diagnose: String? = null
    open var status: Status? = null
        set(value) {
            field = value
            this.statusName = value?.description
        }

    @JsonSerialize(using = MessageJsonSerializer::class)
    open var statusName: String? = null

    @JsonAlias("created_at")
    open var createdAt: Instant? = null

    @JsonAlias("created_by")
    open var createdBy: Long? = null

    @JsonAlias("updated_at")
    open var updatedAt: Instant? = null

    @JsonAlias("updated_by")
    open var updatedBy: Long? = null
}