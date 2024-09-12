package com.example.config;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/com/example/api/v1/demo")
public class Controller
{

    @GetMapping("/all")
    public String hello(){

        return "oumaoumaouamaaaaaimaa";
    }
}