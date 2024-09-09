package io.github.jonaskahn.controllers.storage

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.Instant

class CancerMedicalRecordRequest {
    var department: String? = null

    @field:JsonAlias("save_number", "saveNumber")
    var saveNumber: Int? = null

    @field:JsonAlias("save_year", "saveYear")
    var saveYear: Int? = null

    @field:JsonAlias("patient_number", "patientNumber")
    var patientNumber: Int? = null

    @field:JsonAlias("patient_name", "patientName")
    var patientName: String? = null

    var sex: String? = null

    var birthday: String? = null

    var type: String? = null

    var address: String? = null

    @field:JsonAlias("in_date", "inDate")
    var inDate: Instant? = null

    @field:JsonAlias("out_date", "outDate")
    var outDate: Instant? = null
    
    var diagnose: String? = null
}