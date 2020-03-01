package com.mopon.annotation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mopon.aop.FruitAnnotation;

@RestController
public class AnnotationController {
	@FruitAnnotation
	@GetMapping("/aw1")
	public String test() {
		System.out.println("Annotation....");
		return "Annotation";
	}
	
	@GetMapping("/ab1")
	public String ab() {
		System.out.println("ab1....");
		return "ab1";
	}

}
