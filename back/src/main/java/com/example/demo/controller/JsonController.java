package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class JsonController {

    @GetMapping ("/api")
    public Object home(){ 
        Map hm = new HashMap<String, Object>();
        hm.put("nome", "fulano");
        hm.put("idade", 10);
        return hm;
    }

    @PostMapping("/api")
    public Object post(){
        return "\nta tudo ok\n\n";
    }
}
