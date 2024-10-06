ALTER TABLE patient_request
    MODIFY COLUMN in_date DATETIME NULL,
    MODIFY COLUMN out_date DATETIME NULL,
    MODIFY COLUMN appointment_patient_date DATETIME NULL;
