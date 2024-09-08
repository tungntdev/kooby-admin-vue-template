package io.github.jonaskahn.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "death_record")
open class DeathRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "death_number")
    open var deathNumber: Int? = null

    @Size(max = 255)
    @Column(name = "patient_name")
    open var patientName: String? = null

    @Column(name = "patient_number")
    open var patientNumber: Int? = null

    @Size(max = 50)
    @Column(name = "department", length = 50)
    open var department: String? = null

    @Size(max = 10)
    @Column(name = "sex", length = 10)
    open var sex: String? = null

    @Size(max = 20)
    @Column(name = "birthday", length = 20)
    open var birthday: String? = null

    @Size(max = 1000)
    @Column(name = "address", length = 1000)
    open var address: String? = null

    @Column(name = "in_date")
    open var inDate: Instant? = null

    @Size(max = 255)
    @Column(name = "death_date")
    open var deathDate: String? = null

    @Column(name = "reception_date")
    open var receptionDate: Instant? = null

    @Size(max = 255)
    @Column(name = "submitter")
    open var submitter: String? = null

    @Column(name = "receiver")
    open var receiver: Long? = null

    @Lob
    @Column(name = "diagnosis_of_death")
    open var diagnosisOfDeath: String? = null

    @Column(name = "status_record")
    open var statusRecord: Int? = null

    @Size(max = 255)
    @Column(name = "submitter_record")
    open var submitterRecord: String? = null

    @Column(name = "submitter_record_date")
    open var submitterRecordDate: Instant? = null


    @Column(name = "created_by")
    open var createdBy: Long? = null

    @Column(name = "created_at")
    open var createdAt: Instant? = null

    @Column(name = "updated_by")
    open var updatedBy: Long? = null

    @Column(name = "updated_at")
    open var updatedAt: Instant? = null

    @ColumnDefault("4")
    @Column(name = "status")
    open var status: Int? = null
}