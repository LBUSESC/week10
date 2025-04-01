package com.sesc.unistudycircle.student_service.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "tb_students")
@Data
public class Student  {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "qualification", nullable = false, length = 100)
    private String qualification;

    @Column(name = "university", nullable = false, length = 100)
    private String university;

    public Student() {
    }

    public Student(Long studentId, String firstName, String lastName, String email, String qualification, String university) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.qualification = qualification;
        this.university = university;
    }
}
