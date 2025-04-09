package com.marco.backend.todoapp.backend_todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// @ComponentScan({
// 	"com.marco.backend.cartapp.backend_cartapp.services", 
// 	"com.marco.backend.cartapp.backend_cartapp.controllers",
// 	"com.marco.backend.todoapp.backend_todoapp.repositories",
// 	"com.marco.backend.todoapp.backend_todoapp.models.entities" 
// })
public class BackendTodoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTodoappApplication.class, args);
	}

}
