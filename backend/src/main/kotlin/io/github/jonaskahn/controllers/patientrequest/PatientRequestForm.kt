package io.github.jonaskahn.controllers.patientrequest

import java.time.Instant

class PatientRequestForm {
    var id: Int? = null
    var numberOrder: Int? = null
    var patientNumber: String? = null
    var medicineCode: String? = null
    var patientName: String? = null
    var department: String? = null
    var saveNumber: Int? = null
    var inDate: Instant? = null
    var outDate: Instant? = null
    var receptionDate: Instant? = null
    var appointmentPatientDate: Instant? = null
    var copyQuantity: Int? = null
    var copyCost: Int? = null
    var note: String? = null
    var delivery: Int? = null
    var deliveryOrderNumber: Int? = null
    var deliveryYearOfOrder: Int? = null
    var deliveryAddress: String? = null
    var deliveryPhone: String? = null
    var idProvince: Int? = null
    var idDistrict: Int? = null
    var deliveryCost: Int? = null
}