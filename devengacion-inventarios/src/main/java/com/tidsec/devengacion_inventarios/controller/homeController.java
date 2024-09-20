package com.tidsec.devengacion_inventarios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {
	@GetMapping
	public String home() {
		return "redirect:/technicalGroups";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}
}
