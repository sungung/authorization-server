package com.sung.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NonAuthController {
	@GetMapping("/secret/open/world")
	public String helloWorld(){
		return "Hello World!!!";
	}
}
