package com.example.clinic_skin_be.repository.screen;

import com.example.clinic_skin_be.model.screen.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestimonialRepository extends JpaRepository<Testimonial, Long> {
}
