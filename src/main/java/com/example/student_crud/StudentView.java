package com.example.student_crud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class StudentView extends VerticalLayout {

    private final StudentRepository repository;
    private final Grid<Student> grid = new Grid<>(Student.class, false);

    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");
    private final TextField courseField = new TextField("Course");

    private Student selectedStudent;

    public StudentView(StudentRepository repository) {
        this.repository = repository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new com.vaadin.flow.component.html.H1("Student Management"));

        nameField.setRequired(true);
        emailField.setRequired(true);
        courseField.setRequired(true);

        grid.addColumn(Student::getId).setHeader("ID");
        grid.addColumn(Student::getName).setHeader("Name");
        grid.addColumn(Student::getEmail).setHeader("Email");
        grid.addColumn(Student::getCourse).setHeader("Course");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedStudent = event.getValue();

            if (selectedStudent != null) {
                nameField.setValue(selectedStudent.getName());
                emailField.setValue(selectedStudent.getEmail());
                courseField.setValue(selectedStudent.getCourse());
            } else {
                clearFields();
            }
        });

        Button saveButton = new Button("Save Student", event -> saveStudent());
        Button updateButton = new Button("Update Student", event -> updateStudent());
        Button deleteButton = new Button("Delete Student", event -> deleteStudent());
        Button clearButton = new Button("Clear", event -> {
            grid.asSingleSelect().clear();
            clearFields();
        });

        FormLayout form = new FormLayout(nameField, emailField, courseField);
        HorizontalLayout buttons = new HorizontalLayout(
                saveButton, updateButton, deleteButton, clearButton);

        add(form, buttons, grid);

        refreshGrid();
    }

    private void saveStudent() {
        if (!validFields()) {
            Notification.show("Please fill in all fields.");
            return;
        }

        Student student = new Student(
                nameField.getValue().trim(),
                emailField.getValue().trim(),
                courseField.getValue().trim());

        repository.save(student);
        refreshGrid();
        clearFields();
        Notification.show("Student saved successfully.");
    }

    private void updateStudent() {
        if (selectedStudent == null) {
            Notification.show("Select a student to update.");
            return;
        }

        if (!validFields()) {
            Notification.show("Please fill in all fields.");
            return;
        }

        selectedStudent.setName(nameField.getValue().trim());
        selectedStudent.setEmail(emailField.getValue().trim());
        selectedStudent.setCourse(courseField.getValue().trim());

        repository.save(selectedStudent);
        refreshGrid();
        clearFields();
        Notification.show("Student updated successfully.");
    }

    private void deleteStudent() {
        if (selectedStudent == null) {
            Notification.show("Select a student to delete.");
            return;
        }

        repository.delete(selectedStudent);
        refreshGrid();
        clearFields();
        Notification.show("Student deleted successfully.");
    }

    private boolean validFields() {
        return !nameField.isEmpty()
                && !emailField.isEmpty()
                && !courseField.isEmpty();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        courseField.clear();
        selectedStudent = null;
    }
}
