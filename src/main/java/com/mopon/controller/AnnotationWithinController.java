package com.mopon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mopon.aop.FruitAnnotation;

@FruitAnnotation
@RestController
public class AnnotationWithinController {
	@GetMapping("/aw")
	public String test() {
		System.out.println("AnnotationWithin....");
		return "AnnotationWithin";
	}
	
	@GetMapping("/ab")
	public String ab() {
		System.out.println("ab....");
		return "ab";
	}

}
