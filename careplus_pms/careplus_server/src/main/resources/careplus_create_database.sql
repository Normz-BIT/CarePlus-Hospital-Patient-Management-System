-- =============================================================================
-- CarePlus Hospital Patient Management System
-- =============================================================================

DROP DATABASE IF EXISTS careplus_db;
CREATE DATABASE careplus_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE careplus_db;


-- Person: attributes common to everyone.
CREATE TABLE person (
    person_id   VARCHAR(10)  NOT NULL,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    email       VARCHAR(120) NOT NULL,
    phone       VARCHAR(20),
    password    VARCHAR(255) NOT NULL,        
    role        ENUM('DOCTOR', 'NURSE', 'RECEPTIONIST', 'PATIENT') NOT NULL DEFAULT 'PATIENT',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_person PRIMARY KEY (person_id),
    CONSTRAINT uq_person_email UNIQUE (email)
) ENGINE=InnoDB;

-- Patient.
CREATE TABLE patient (
    person_id       VARCHAR(10) NOT NULL,
    date_of_birth   DATE,
    gender          ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    address         VARCHAR(200),
    medical_history TEXT,
    CONSTRAINT pk_patient PRIMARY KEY (person_id),
    CONSTRAINT fk_patient_person FOREIGN KEY (person_id)
        REFERENCES person (person_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Employee
CREATE TABLE employee (
    person_id   VARCHAR(10) NOT NULL,
    department  VARCHAR(80),
    hire_date   DATE,
    CONSTRAINT pk_employee PRIMARY KEY (person_id),
    CONSTRAINT fk_employee_person FOREIGN KEY (person_id)
        REFERENCES person (person_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- DOCTOR
CREATE TABLE doctor (
    person_id      VARCHAR(10) NOT NULL,
    specialization ENUM('GENERAL', 'DERMATOLOGY', 'CARDIOLOGY', 'PEDIATRICS', 'PSYCHIATRY') NOT NULL,
    license_no     VARCHAR(40),
    CONSTRAINT pk_doctor PRIMARY KEY (person_id),
    CONSTRAINT fk_doctor_employee FOREIGN KEY (person_id)
        REFERENCES employee (person_id) ON DELETE CASCADE,
    CONSTRAINT uq_doctor_license UNIQUE (license_no)
) ENGINE=InnoDB;


-- Nurse
CREATE TABLE nurse (
    person_id VARCHAR(10) NOT NULL,
    ward      ENUM('GENERAL', 'PEDIATRIC', 'NEONATAL', 'ONCOLOGY', 'EMERGENCY') NOT NULL,
    CONSTRAINT pk_nurse PRIMARY KEY (person_id),
    CONSTRAINT fk_nurse_employee FOREIGN KEY (person_id)
        REFERENCES employee (person_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- RECEPTIONIST
CREATE TABLE receptionist (
    person_id VARCHAR(10) NOT NULL,
    desk_no   VARCHAR(20),
    CONSTRAINT pk_receptionist PRIMARY KEY (person_id),
    CONSTRAINT fk_receptionist_employee FOREIGN KEY (person_id)
        REFERENCES employee (person_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Complaint: patient medical requests.
CREATE TABLE complaint (
    complaint_id      INT          NOT NULL AUTO_INCREMENT,
    patient_id        VARCHAR(10)  NOT NULL,
    complaintParentId INT          NULL,
    category          ENUM('GENERAL_HEALTH', 'MEDICATION_CONCERN', 'APPOINTMENT_ISSUE') NOT NULL, 
    description       TEXT         NOT NULL,
    date_submitted    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status            ENUM('SUBMITTED', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'REOPENED') NOT NULL DEFAULT 'SUBMITTED',
    response          TEXT,
    response_date     DATETIME,
    responded_by      VARCHAR(10),                  
    assigned_to       VARCHAR(10),                 
    CONSTRAINT pk_complaint PRIMARY KEY (complaint_id),
    CONSTRAINT fk_complaint_patient FOREIGN KEY (patient_id)
        REFERENCES patient (person_id) ON DELETE CASCADE,
    CONSTRAINT fk_complaint_parent FOREIGN KEY (complaintParentId) 
        REFERENCES complaint(complaint_id) ON DELETE SET NULL ON UPDATE CASCADE,	
    CONSTRAINT fk_complaint_responder FOREIGN KEY (responded_by)
        REFERENCES employee (person_id) ON DELETE SET NULL,
    CONSTRAINT fk_complaint_assignee FOREIGN KEY (assigned_to)
        REFERENCES employee (person_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Appointment.
CREATE TABLE appointment (
    appointment_id   INT          NOT NULL AUTO_INCREMENT,
    patient_id       VARCHAR(10)  NOT NULL,
    doctor_id        VARCHAR(10)  NOT NULL,
    appointment_date DATETIME     NOT NULL,
    reason           VARCHAR(200),
    status           ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT pk_appointment PRIMARY KEY (appointment_id),
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id)
        REFERENCES patient (person_id) ON DELETE CASCADE,
    CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id)
        REFERENCES doctor (person_id)
) ENGINE=InnoDB;

-- Medical record
CREATE TABLE medical_record (
    record_id       INT          NOT NULL AUTO_INCREMENT,
    patient_id      VARCHAR(10)  NOT NULL,
    doctor_id       VARCHAR(10)  NOT NULL,
    diagnosis       TEXT,
    treatment_notes TEXT,
    follow_up_date  DATE,
    created_date    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_medical_record PRIMARY KEY (record_id),
    CONSTRAINT fk_record_patient FOREIGN KEY (patient_id)
        REFERENCES patient (person_id) ON DELETE CASCADE,
    CONSTRAINT fk_record_doctor FOREIGN KEY (doctor_id)
        REFERENCES doctor (person_id)
) ENGINE=InnoDB;

-- Vital signs: nurse observations.
CREATE TABLE vital_signs (
    vital_id         INT          NOT NULL AUTO_INCREMENT,
    patient_id       VARCHAR(10)  NOT NULL,
    nurse_id         VARCHAR(10)  NOT NULL,
    temperature      DECIMAL(4,1),              
    blood_pressure   VARCHAR(10),                
    heart_rate       INT,
    respiratory_rate INT,
    observations     TEXT,
    nursing_notes    TEXT,
    recorded_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_vital_signs PRIMARY KEY (vital_id),
    CONSTRAINT fk_vitals_patient FOREIGN KEY (patient_id)
        REFERENCES patient (person_id) ON DELETE CASCADE,
    CONSTRAINT fk_vitals_nurse FOREIGN KEY (nurse_id)
        REFERENCES nurse (person_id),
    CONSTRAINT chk_vitals_hr   CHECK (heart_rate IS NULL OR heart_rate >= 0),  
    CONSTRAINT chk_vitals_temp CHECK (temperature IS NULL OR temperature > 0)   
) ENGINE=InnoDB;

-- Payment.
CREATE TABLE payment (
    payment_id          INT           NOT NULL AUTO_INCREMENT,
    patient_id          VARCHAR(10)   NOT NULL,
    amount_paid         DECIMAL(10,2) NOT NULL,
    payment_date        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    outstanding_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    description         VARCHAR(200),
    CONSTRAINT pk_payment PRIMARY KEY (payment_id),
    CONSTRAINT fk_payment_patient FOREIGN KEY (patient_id)
        REFERENCES patient (person_id) ON DELETE CASCADE,
    CONSTRAINT chk_payment_amount  CHECK (amount_paid >= 0),        
    CONSTRAINT chk_payment_balance CHECK (outstanding_balance >= 0)
) ENGINE=InnoDB;

-- Chat message 
CREATE TABLE chat_message (
    message_id  INT          NOT NULL AUTO_INCREMENT,
    sender_id   VARCHAR(10)  NOT NULL,
    receiver_id VARCHAR(10)  NOT NULL,
    content     TEXT         NOT NULL,
    sent_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read     BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_chat_message PRIMARY KEY (message_id),
    CONSTRAINT fk_chat_sender FOREIGN KEY (sender_id)
        REFERENCES person (person_id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_receiver FOREIGN KEY (receiver_id)
        REFERENCES person (person_id) ON DELETE CASCADE
) ENGINE=InnoDB;


-- INDEXES 
CREATE INDEX idx_complaint_status   ON complaint (status);
CREATE INDEX idx_complaint_category ON complaint (category);
CREATE INDEX idx_appt_date          ON appointment (appointment_date);
CREATE INDEX idx_chat_inbox         ON chat_message (receiver_id, is_read);


-- SAMPLE DATA  (load before presentation)

-- NOTE: Do not put a semicolon inside a string literal in this file.
-- DatabaseResetService splits statements on the semicolon character, so one
-- inside quoted text breaks that statement in two. Use a comma or a full stop
-- instead. (This note deliberately spells out the character rather than using
-- it, for the same reason.)

-- People: patients
INSERT INTO person (person_id, first_name, last_name, email, phone, password, role) VALUES
 ('PAT0001', 'Andre',    'Campbell', 'andre.campbell@example.com',  '876-555-0101', 'pat123','PATIENT'),
 ('PAT0002', 'Shanice',  'Brown',    'shanice.brown@example.com',   '876-555-0102', 'pat123','PATIENT'),
 ('PAT0003', 'Marcus',   'Thompson', 'marcus.thompson@example.com', '876-555-0103', 'pat123','PATIENT'),
 ('PAT0004', 'Taneisha', 'Walker',   'taneisha.walker@example.com', '876-555-0104', 'pat123','PATIENT');

INSERT INTO patient (person_id, date_of_birth, gender, address, medical_history) VALUES
 ('PAT0001', '1990-04-12', 'MALE',   '12 Hope Road, Kingston',      'Hypertension (controlled).'),
 ('PAT0002', '1985-11-23', 'FEMALE', '5 Constant Spring, Kingston', 'Type 2 diabetes, penicillin allergy.'),
 ('PAT0003', '2001-07-08', 'MALE',   '88 Red Hills Road, Kingston', 'No significant history.'),
 ('PAT0004', '1978-02-19', 'FEMALE', '3 Mona Heights, Kingston',    'Asthma.');

-- People: staff 
INSERT INTO person (person_id, first_name, last_name, email, phone, password, role) VALUES
 ('STF0001', 'Karen',   'Reid',     'k.reid@careplus.example',    '876-555-0201', 'staff123','DOCTOR'),
 ('STF0002', 'David',   'Henry',    'd.henry@careplus.example',   '876-555-0202', 'staff123','DOCTOR'),
 ('STF0003', 'Paula',   'Grant',    'p.grant@careplus.example',   '876-555-0203', 'staff123','NURSE'),
 ('STF0004', 'Michael', 'Forbes',   'm.forbes@careplus.example',  '876-555-0204', 'staff123','NURSE'),
 ('STF0005', 'Janet',   'Williams', 'j.williams@careplus.example','876-555-0205', 'staff123', 'RECEPTIONIST');

INSERT INTO employee (person_id, department, hire_date) VALUES
 ('STF0001', 'Internal Medicine', '2018-06-01'),
 ('STF0002', 'Cardiology',        '2020-09-15'),
 ('STF0003', 'General Ward',      '2019-03-10'),
 ('STF0004', 'Emergency',         '2021-01-20'),
 ('STF0005', 'Front Desk',        '2022-05-05');

INSERT INTO doctor (person_id, specialization, license_no) VALUES
 ('STF0001', 'GENERAL',    'MD-JM-1001'),
 ('STF0002', 'CARDIOLOGY', 'MD-JM-1002');

INSERT INTO nurse (person_id, ward) VALUES
 ('STF0003', 'GENERAL'),
 ('STF0004', 'EMERGENCY');

INSERT INTO receptionist (person_id, desk_no) VALUES
 ('STF0005', 'D-01');

-- Complaints
INSERT INTO complaint (patient_id, category, description, date_submitted, status, response, response_date, responded_by, assigned_to) VALUES
 ('PAT0001', 'GENERAL_HEALTH',     'Persistent headaches for the past week.',         '2026-06-10 09:15:00', 'RESOLVED',    'Assigned to Dr. Reid. Please book a consultation.', '2026-06-10 11:00:00', 'STF0005', 'STF0001'),
 ('PAT0002', 'MEDICATION_CONCERN', 'Unsure about my new diabetes medication dosage.', '2026-06-12 14:30:00', 'IN_PROGRESS', 'Dr. Henry will review and respond shortly.',        '2026-06-12 15:10:00', 'STF0005', 'STF0002'),
 ('PAT0003', 'APPOINTMENT_ISSUE',  'I need to reschedule my appointment.',            '2026-06-15 10:05:00', 'ASSIGNED',    NULL, NULL, 'STF0005', 'STF0005'),
 ('PAT0004', 'GENERAL_HEALTH',     'Shortness of breath after exercise.',             '2026-06-18 08:45:00', 'SUBMITTED',   NULL, NULL, NULL, NULL);

-- Appointments
INSERT INTO appointment (patient_id, doctor_id, appointment_date, reason, status) VALUES
 ('PAT0001', 'STF0001', '2026-06-25 10:00:00', 'Headache follow-up',     'SCHEDULED'),
 ('PAT0002', 'STF0002', '2026-06-26 13:30:00', 'Medication review',      'SCHEDULED'),
 ('PAT0001', 'STF0001', '2026-06-05 09:00:00', 'Initial consultation',   'COMPLETED'),
 ('PAT0004', 'STF0002', '2026-07-01 11:00:00', 'Respiratory assessment', 'SCHEDULED');

-- Medical records
INSERT INTO medical_record (patient_id, doctor_id, diagnosis, treatment_notes, follow_up_date) VALUES
 ('PAT0001', 'STF0001', 'Tension headache',       'Advised rest, hydration, OTC analgesics.',      '2026-06-25'),
 ('PAT0002', 'STF0002', 'Stable type 2 diabetes', 'Continue current medication, monitor glucose.', '2026-07-10');

-- Vital signs
INSERT INTO vital_signs (patient_id, nurse_id, temperature, blood_pressure, heart_rate, respiratory_rate, observations, nursing_notes) VALUES
 ('PAT0001', 'STF0003', 36.8, '128/82', 76, 16, 'Patient alert and oriented.', 'No acute distress.'),
 ('PAT0004', 'STF0004', 37.1, '118/76', 88, 20, 'Mild wheezing on exhale.',    'Monitor respiratory status.');

-- Payments 
INSERT INTO payment (patient_id, amount_paid, payment_date, outstanding_balance, description) VALUES
 ('PAT0001', 5000.00, '2026-06-05 12:00:00', 0.00,    'Consultation fee'),
 ('PAT0002', 3000.00, '2026-06-12 16:00:00', 2000.00, 'Lab tests (partial payment)'),
 ('PAT0003', 0.00,    '2026-06-15 10:30:00', 4500.00, 'Pending appointment fee'),
 ('PAT0004', 7500.00, '2026-06-18 09:30:00', 0.00,    'Respiratory assessment');

-- Chat messages 
INSERT INTO chat_message (sender_id, receiver_id, content, sent_at, is_read) VALUES
 ('PAT0001', 'STF0005', 'Hi, has my complaint been reviewed?',            '2026-06-10 09:20:00', TRUE),
 ('STF0005', 'PAT0001', 'Yes, you have been assigned to Dr. Reid.',       '2026-06-10 09:25:00', TRUE),
 ('PAT0002', 'STF0002', 'Doctor, should I take the medication at night?', '2026-06-12 15:30:00', FALSE);
