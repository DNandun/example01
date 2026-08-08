package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
public class StudentViewController {

    private final StudentService studentService;

    @Autowired
    public StudentViewController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Student> students = (keyword != null && !keyword.isBlank())
                ? studentService.searchStudents(keyword)
                : studentService.getAllStudents();

        double avgGpa = students.isEmpty() ? 0.0 : students.stream()
                .filter(s -> s.getGpa() != null)
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);

        long activeCount = students.stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .count();

        long deptCount = students.stream()
                .map(Student::getDepartment)
                .filter(d -> d != null && !d.isBlank())
                .distinct()
                .count();

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("avgGpa", String.format("%.2f", avgGpa));
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("deptCount", deptCount);

        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Student());
        }

        return "index";
    }

    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.student", result);
            redirectAttributes.addFlashAttribute("student", student);
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check your input.");
            return "redirect:/";
        }

        try {
            if (student.getId() == null) {
                studentService.createStudent(student);
                redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
            } else {
                studentService.updateStudent(student.getId(), student);
                redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("student", student);
        }

        return "redirect:/";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}
