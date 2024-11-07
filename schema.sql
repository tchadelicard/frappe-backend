DROP TABLE IF EXISTS internship_requests;
DROP TABLE IF EXISTS actions;
DROP TABLE IF EXISTS meeting_requests;
DROP TABLE IF EXISTS students_specialties_per_year;
DROP TABLE IF EXISTS campuses_specialties;
DROP TABLE IF EXISTS specialties;
DROP TABLE IF EXISTS campuses;
DROP TABLE IF EXISTS credit_transfers;
DROP TABLE IF EXISTS students_curriculums;
DROP TABLE IF EXISTS curriculums;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS supervisors;
DROP TABLE IF EXISTS users;

CREATE TABLE campuses (
    campus_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE specialties (
    specialty_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    campus_id INTEGER NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campuses(campus_id)
);

CREATE TABLE supervisors (
    supervisor_id BIGINT PRIMARY KEY,
    meeting_url VARCHAR(255),
    caldav_username VARCHAR(255),
    caldav_password VARCHAR(255),
    FOREIGN KEY (supervisor_id) REFERENCES users(user_id)
);

CREATE TABLE credit_transfers (
    credit_transfer_id BIGSERIAL PRIMARY KEY,
    university VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE students (
    student_id BIGINT PRIMARY KEY,
    gender VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    credit_transfer_id INTEGER,
    FOREIGN KEY (student_id) REFERENCES users(user_id),
    FOREIGN KEY (credit_transfer_id) REFERENCES credit_transfers(credit_transfer_id)
);

CREATE TABLE curriculums (
    curriculum_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE students_curriculums (
    student_id BIGINT NOT NULL,
    curriculum_id BIGINT NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (curriculum_id) REFERENCES curriculums(curriculum_id),
    PRIMARY KEY (student_id, curriculum_id)
);

CREATE TABLE campuses_specialties (
    campus_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campuses(campus_id),
    FOREIGN KEY (specialty_id) REFERENCES specialties(specialty_id),
    PRIMARY KEY (campus_id, specialty_id)
);

CREATE TABLE students_specialties_per_year (
    student_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (specialty_id) REFERENCES specialties(specialty_id),
    PRIMARY KEY (student_id, specialty_id)
);

CREATE TABLE meeting_requests (
    meeting_request_id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    duration INTEGER NOT NULL,
    theme VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    request_description TEXT NOT NULL,
    status VARCHAR(255) NOT NULL,
    action_id BIGINT,
    student_id BIGINT NOT NULL,
    supervisor_id BIGINT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (supervisor_id) REFERENCES supervisors(supervisor_id)
);

CREATE TABLE actions (
    id BIGSERIAL PRIMARY KEY,
    notes TEXT NOT NULL,
    action_plan TEXT NOT NULL
);

CREATE TABLE internship_requests (
    internship_request_id BIGINT PRIMARY KEY,
    internship_duration INTEGER NOT NULL,
    wanted_city VARCHAR(255) NOT NULL,
    wanted_country VARCHAR(255) NOT NULL,
    FOREIGN KEY (internship_request_id) REFERENCES meeting_requests(meeting_request_id)
);
