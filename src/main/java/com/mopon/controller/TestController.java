package com.mopon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/c")
	public String test() {
		System.out.println("c....");
		return "c....";
	}
	

	@GetMapping("/d")
	public String b() {
		System.out.println("ddd....");
		return "ddd";
	}
}
