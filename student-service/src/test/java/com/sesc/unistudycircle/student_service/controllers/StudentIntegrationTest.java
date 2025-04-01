package com.sesc.unistudycircle.student_service.controllers;

import com.sesc.unistudycircle.student_service.entities.Student;
import com.sesc.unistudycircle.student_service.repositories.StudentRepository;
import com.sesc.unistudycircle.student_service.services.StudentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentIntegrationTest {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    TestRestTemplate testRestTemplate;
    private Student student;

    @Container
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");


    @Test
    void contextLoads() {
        mySQLContainer.start();
        assertTrue(mySQLContainer.isRunning());
    }

    @BeforeEach
    void setUp() {
         student = new Student(111l,"Satish","Kumar",
                "s.kumar@leedsbeckett.ac.uk","PhD",
                 "Leeds Beckett University");
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }


    @Test
    void shouldCreateStudentRecord() {
        // Given - student object is created in the setUp method

        // When - send a POST request to create a student
        ResponseEntity<Student> response = testRestTemplate
                .postForEntity("/student/create", student, Student.class);

        // Then - verify the response

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getStudentId()).isEqualTo(111l);
        assertThat(response.getBody().getFirstName()).isEqualTo("Satish");
        assertThat(response.getBody().getLastName()).isEqualTo("Kumar");

    }

    @Test
    void shouldGetStudentById() {
        // Given - a student is created in the database
        studentService.saveStudent(student);

        // When - send a GET request to retrieve the student by ID
        ResponseEntity<Student> response = testRestTemplate.getForEntity("/student/" + student
                .getStudentId(), Student.class);

        // Then - verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualTo(student.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo(student.getLastName());
        assertThat(response.getBody().getEmail()).isEqualTo("s.kumar@leedsbeckett.ac.uk");
    }

    @Test
    void shouldUpdateStudentById() {
        // Given - a student is created in the database
        studentService.saveStudent(student);

        // When - send a PUT request to update the student
        Student updatedStudent = new Student(111l, "Satish", "Malik",
                "satish@bham.ac.uk", "PhD", "University of Birmingham");

        ResponseEntity<Student> response = testRestTemplate.exchange("/student/" + student
                        .getStudentId(), HttpMethod.PUT,
                new HttpEntity<>(updatedStudent), Student.class);

        // Then - verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStudentId()).isEqualTo(student.getStudentId());
        assertThat(response.getBody().getFirstName()).isEqualTo(updatedStudent.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo("Malik");
        assertThat(response.getBody().getEmail()).isEqualTo(updatedStudent.getEmail());
        assertThat(response.getBody().getUniversity()).isEqualTo("University of Birmingham");
    }

    @Test
    void shouldDeleteStudentById() {
        // Given - a student is created in the database
        studentService.saveStudent(student);

        // When - send a DELETE request to delete the student by ID
        ResponseEntity<Void> response = testRestTemplate.exchange("/student/" + student
                .getStudentId(), HttpMethod.DELETE, null, Void.class);

        // Then - verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}