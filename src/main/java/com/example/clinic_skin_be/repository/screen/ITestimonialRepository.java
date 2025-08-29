package com.example.clinic_skin_be.repository.screen;

import com.example.clinic_skin_be.model.screen.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ITestimonialRepository extends JpaRepository<Testimonial, Long> {
    @Query("SELECT t.id AS testimonialId, t.content AS content, t.img AS img, t.account.id AS accountId, t.account.fullName as fullName FROM Testimonial t")
    List<Map<String, Object>> findAllTestimonials();
}
