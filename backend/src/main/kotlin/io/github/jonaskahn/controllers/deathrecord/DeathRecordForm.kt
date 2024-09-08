package io.github.jonaskahn.controllers.deathrecord

import jakarta.persistence.Column
import java.time.Instant

class DeathRecordForm {
    var id: Int? = null

    @Column(name = "death_number")
    var deathNumber: Int? = null

    @Column(name = "patient_name")
    var patientName: String? = null

    @Column(name = "patient_number")
    var patientNumber: Int? = null
    var department: String? = null
    var sex: String? = null
    var birthday: String? = null
    var address: String? = null

    @Column(name = "death_date")
    var deathDate: String? = null

    @Column(name = "reception_date")
    var receptionDate: Instant? = null
    var submitter: String? = null
    var receiver: Long? = null

    @Column(name = "diagnosis_of_death")
    var diagnosisOfDeath: String? = null

    @Column(name = "status_record")
    var statusRecord: Int? = null

    @Column(name = "submitter_record")
    var submitterRecord: String? = null

    @Column(name = "submitter_record_date")
    var submitterRecordDate: Instant? = null
}