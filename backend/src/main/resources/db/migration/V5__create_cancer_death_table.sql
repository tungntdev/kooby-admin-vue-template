create table if not exists cancer_medical_record
(
    id             int auto_increment primary key,
    department     varchar(50)   null,
    save_number    int           null,
    save_year      int           null,
    patient_number int           null,
    patient_name   varchar(255)  null,
    sex            varchar(10)   null,
    birthday       varbinary(50) null,
    type           varchar(50)   null,
    address        varchar(1000) null,
    in_date        datetime      null,
    out_date       datetime      null,
    diagnose       text          null,
    status         int default 1 null,
    created_by     int           null,
    created_at     datetime      null,
    updated_by     int           null,
    updated_at     datetime      null
);

create table if not exists death_record
(
    id                     int auto_increment primary key,
    death_number           int           null,
    patient_name           varchar(255)  null,
    patient_number         int           null,
    department             varchar(50)   null,
    sex                    varchar(10)   null,
    birthday               varchar(20)   null,
    address                varchar(1000) null,
    in_date                datetime      null,
    death_date             varchar(255)  null,
    reception_date         datetime      null,
    submitter              varchar(255)  null,
    receiver               int           null,
    diagnosis_of_death     text          null,
    status_record          int           null,
    submitter_record       varchar(255)  null,
    submitter_record_date  datetime      null,
    status                 int default 1 null,
    created_by             int           null,
    created_at             datetime      null,
    updated_by             int           null,
    updated_at             datetime      null
);
