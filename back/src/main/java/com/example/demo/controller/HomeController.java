package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {

  /**
   * ben
   * benspassword
   * @return
   */
  @GetMapping("/")
  public String index() {
    return "Welcome to the home page!";
  }

  @RequestMapping("/dashboard")
	public Object firstPage() {
    HashMap m = new HashMap<String, Object>();

    m.put("success", true);

		return m;
	}
}