package com.example.demo.service.impl;

import com.example.demo.entity.Student;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateEmailException("Email address '" + student.getEmail() + "' is already registered");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);

        if (studentRepository.existsByEmailAndIdNot(studentDetails.getEmail(), id)) {
            throw new DuplicateEmailException("Email address '" + studentDetails.getEmail() + "' is already in use by another student");
        }

        existingStudent.setFirstName(studentDetails.getFirstName());
        existingStudent.setLastName(studentDetails.getLastName());
        existingStudent.setEmail(studentDetails.getEmail());
        existingStudent.setDepartment(studentDetails.getDepartment());
        existingStudent.setAge(studentDetails.getAge());
        existingStudent.setGpa(studentDetails.getGpa());
        if (studentDetails.getStatus() != null && !studentDetails.getStatus().isBlank()) {
            existingStudent.setStatus(studentDetails.getStatus());
        }

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.searchStudents(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartmentIgnoreCase(department);
    }
}
