package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleService {

	@GetMapping("/message")
	@ResponseBody
	public String getMessage() {
		return "This is the message";
	}
	
}
