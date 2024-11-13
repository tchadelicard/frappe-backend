package fr.imt_atlantique.frappe.controllers;

import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
public class HelloWorldController {
    
    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }


    
}
