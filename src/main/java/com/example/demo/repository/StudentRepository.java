package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findByDepartmentIgnoreCase(String department);

    List<Student> findByStatus(String status);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);
}
