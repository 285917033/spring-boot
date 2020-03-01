package com.mopon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/test")
	public String test() {
		System.out.println("Hello....");
		return "hello";
	}
	

	@GetMapping("/b")
	public String b() {
		System.out.println("b....");
		return "b";
	}
}
