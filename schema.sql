DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS supervisor;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS student_cirriculum;
DROP TABLE IF EXISTS cirriculum;
DROP TABLE IF EXISTS credit_transfer;
DROP TABLE IF EXISTS campus;
DROP TABLE IF EXISTS campus_specialty;
DROP TABLE IF EXISTS specialty;
DROP TABLE IF EXISTS student_specialty_per_year;
DROP TABLE IF EXISTS meeting_request;
DROP TABLE IF EXISTS internship_request;
DROP TABLE IF EXISTS action;

CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    campus_id INTEGER NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campus(id)
);

CREATE TABLE supervisor (
    id INTEGER PRIMARY KEY,
    meeting_url VARCHAR(255),
    caldav_username VARCHAR(255),
    caldav_password VARCHAR(255),
    FOREIGN KEY (id) REFERENCES user(id)
)

CREATE TABLE student (
    id INTEGER PRIMARY KEY,
    gender VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    credit_transfer_id INTEGER,
    FOREIGN KEY (id) REFERENCES user(id),
    FOREIGN KEY (credit_transfer_id) REFERENCES credit_transfer(id)
);

CREATE TABLE student_cirriculum (
    student_id INTEGER NOT NULL,
    cirriculum_id INTEGER NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (cirriculum_id) REFERENCES cirriculum(id),
    PRIMARY KEY (student_id, cirriculum_id)
);

CREATE TABLE cirriculum (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE credit_transfer (
    id SERIAL PRIMARY KEY,
    university VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    student_id INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE TABLE campus (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE campus_specialty (
    campus_id INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL,
    FOREIGN KEY (campus_id) REFERENCES campus(id),
    FOREIGN KEY (specialty_id) REFERENCES specialty(id),
    PRIMARY KEY (campus_id, specialty_id)
);

CREATE TABLE specialty (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE student_specialty_per_year (
    student_id INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL,
    year INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (specialty_id) REFERENCES specialty(id),
    PRIMARY KEY (student_id, specialty_id)
);

CREATE TABLE meeting_request (
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
    FOREIGN KEY (action_id) REFERENCES action(id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (supervisor_id) REFERENCES supervisor(id)
);

CREATE TABLE internship_request (
    id INTEGER PRIMARY KEY,
    internship_duration INTEGER NOT NULL,
    wanted_city VARCHAR(255) NOT NULL,
    wanted_country VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES meeting_request(id)
)

CREATE TABLE action (
    id SERIAL PRIMARY KEY,
    notes TEXT NOT NULL,
    action_plan TEXT NOT NULL,
    meeting_request_id INTEGER NOT NULL,
    FOREIGN KEY (meeting_request_id) REFERENCES meeting_request(id)
);
