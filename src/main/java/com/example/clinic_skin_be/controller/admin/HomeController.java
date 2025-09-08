package com.example.clinic_skin_be.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/admin/index")
    public String index () {
        System.out.println("Đã vào được đây");
        return "admin/home";
    }
}
