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
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE specialties (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    campus_id INTEGER NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campuses(id)
);

CREATE TABLE supervisors (
    id INTEGER PRIMARY KEY,
    meeting_url VARCHAR(255),
    caldav_username VARCHAR(255),
    caldav_password VARCHAR(255),
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE credit_transfers (
    id SERIAL PRIMARY KEY,
    university VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE students (
    id INTEGER PRIMARY KEY,
    gender VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    credit_transfer_id INTEGER,
    FOREIGN KEY (id) REFERENCES users(id),
    FOREIGN KEY (credit_transfer_id) REFERENCES credit_transfers(id)
);

CREATE TABLE curriculums (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE students_curriculums (
    student_id INTEGER NOT NULL,
    curriculum_id INTEGER NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (curriculum_id) REFERENCES curriculums(id),
    PRIMARY KEY (student_id, curriculum_id)
);

CREATE TABLE campuses_specialties (
    campus_id INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campuses(id),
    FOREIGN KEY (specialty_id) REFERENCES specialties(id),
    PRIMARY KEY (campus_id, specialty_id)
);

CREATE TABLE students_specialties_per_year (
    student_id INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (specialty_id) REFERENCES specialties(id),
    PRIMARY KEY (student_id, specialty_id)
);

CREATE TABLE meeting_requests (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    duration INTEGER NOT NULL,
    theme VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    request_description TEXT NOT NULL,
    status VARCHAR(255) NOT NULL,
    action_id INTEGER,
    student_id INTEGER NOT NULL,
    supervisor_id INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (supervisor_id) REFERENCES supervisors(id)
);

CREATE TABLE actions (
    id SERIAL PRIMARY KEY,
    notes TEXT NOT NULL,
    action_plan TEXT NOT NULL
);

CREATE TABLE internship_requests (
    id INTEGER PRIMARY KEY,
    internship_duration INTEGER NOT NULL,
    wanted_city VARCHAR(255) NOT NULL,
    wanted_country VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES meeting_requests(id)
);
