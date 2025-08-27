package com.example.clinic_skin_be.controller.screen;

import com.example.clinic_skin_be.repository.screen.ITestimonialRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testimonials")
@AllArgsConstructor
public class TestimonialRestController {
    @Autowired
    private ITestimonialRepository testimonialRepository;

    @GetMapping("")
    public List<Map<String, Object>> getAllTestimonials () {
        return testimonialRepository.findAllTestimonials();
    }
}
