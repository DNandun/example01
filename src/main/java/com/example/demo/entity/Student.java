package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(name = "department", nullable = false)
    private String department;

    @Min(value = 16, message = "Age should not be less than 16")
    @Max(value = 100, message = "Age should not be greater than 100")
    @Column(name = "age")
    private Integer age;

    @DecimalMin(value = "0.0", message = "GPA cannot be negative")
    @DecimalMax(value = "4.0", message = "GPA cannot exceed 4.0")
    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "status")
    private String status; // ACTIVE, INACTIVE, GRADUATED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String email, String department, Integer age, Double gpa, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.age = age;
        this.gpa = gpa;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null || this.status.isBlank()) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String department;
        private Integer age;
        private Double gpa;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StudentBuilder department(String department) {
            this.department = department;
            return this;
        }

        public StudentBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public StudentBuilder gpa(Double gpa) {
            this.gpa = gpa;
            return this;
        }

        public StudentBuilder status(String status) {
            this.status = status;
            return this;
        }

        public StudentBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudentBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, email, department, age, gpa, status, createdAt, updatedAt);
        }
    }
}
