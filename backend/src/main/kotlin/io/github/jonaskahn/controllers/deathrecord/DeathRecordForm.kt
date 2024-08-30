package io.github.jonaskahn.controllers.deathrecord

import io.github.jonaskahn.entities.converter.StatusConverter
import io.github.jonaskahn.entities.enums.Status
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Lob
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

class DeathRecordForm {
    var id: Int? = null
    var deathNumber: Int? = null
    var patientName: String? = null
    var patientNumber: Int? = null
    var department: String? = null
    var sex: String? = null
    var birthday: String? = null
    var address: String? = null
    var deathDate: String? = null
    var receptionDate: Instant? = null
    var submitter: String? = null
    var receiver: Int? = null
    var diagnosisOfDeath: String? = null
    var statusRecord: Int? = null
    var submitterRecord: String? = null
    var submitterRecordDate: Instant? = null
}