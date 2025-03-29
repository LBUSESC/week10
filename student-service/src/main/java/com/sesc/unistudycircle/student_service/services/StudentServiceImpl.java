package com.sesc.unistudycircle.student_service.services;

import com.sesc.unistudycircle.student_service.entities.Student;
import com.sesc.unistudycircle.student_service.exceptions.StudentNotFoundException;
import com.sesc.unistudycircle.student_service.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {

        this.repository = repository;
    }

    @Override
    public Student saveStudent(Student student) {
        log.info("Save Student() from Service Layer: {}");
        //Add additional validation logic if required
        return repository.save(student);
    }

    @Override
    public Student getStudentById(long studentId) {
        log.info("Get Student() from Service Layer: {}");
        return repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));
    }

    @Override
    public void deleteStudentById(long studentId) {
        log.info("Delete Student() from Service Layer: {}");
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        repository.deleteById(studentId);
    }

    @Override
    public Student updateStudentById(long studentId, Student updatedStudent) {
        log.info("Update Student() from Service Layer: {}");
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        //Add additional validation logic if required
        updatedStudent.setStudentId(studentId);
        return repository.save(updatedStudent);
    }

    public List<Student> getAllStudents() {

        return repository.findAll();
    }
}
