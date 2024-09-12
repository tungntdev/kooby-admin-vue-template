package io.github.jonaskahn.entities

import io.github.jonaskahn.entities.converter.StatusConverter
import io.github.jonaskahn.entities.enums.Status
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "cancer_medical_record")
open class CancerMedicalRecord : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Size(max = 50)
    @Column(name = "department", length = 50)
    open var department: String? = null

    @Column(name = "save_number")
    open var saveNumber: Int? = null

    @Column(name = "save_year")
    open var saveYear: Int? = null

    @Column(name = "patient_number")
    open var patientNumber: Int? = null

    @Size(max = 255)
    @Column(name = "patient_name")
    open var patientName: String? = null

    @Size(max = 10)
    @Column(name = "sex", length = 10)
    open var sex: String? = null

    @Size(max = 50)
    @Column(name = "birthday", length = 50)
    open var birthday: String? = null

    @Size(max = 50)
    @Column(name = "type", length = 50)
    open var type: String? = null

    @Size(max = 1000)
    @Column(name = "address", length = 1000)
    open var address: String? = null

    @Column(name = "in_date")
    open var inDate: Instant? = null

    @Column(name = "out_date")
    open var outDate: Instant? = null

    @Lob
    @Column(name = "diagnose")
    open var diagnose: String? = null

    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    @Convert(converter = StatusConverter::class)
    open var status: Status? = Status.ACTIVATED
}