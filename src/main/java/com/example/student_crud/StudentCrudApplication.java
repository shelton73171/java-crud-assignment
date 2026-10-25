package com.example.student_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentCrudApplication {

	public static void main(String[] args) {
		System.out.println("Student CRUD application is starting...");
		SpringApplication.run(StudentCrudApplication.class, args);
	}

}
