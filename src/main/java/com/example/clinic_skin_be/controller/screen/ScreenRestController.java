package com.example.clinic_skin_be.controller.screen;

import com.example.clinic_skin_be.dto.screen.TestimonialInfoDTO;
import com.example.clinic_skin_be.model.screen.Offer;
import com.example.clinic_skin_be.model.screen.Testimonial;
import com.example.clinic_skin_be.repository.screen.IOfferRepository;
import com.example.clinic_skin_be.repository.screen.ITestimonialRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/screen-dashboard")
@AllArgsConstructor
public class ScreenRestController {
    private final IOfferRepository offerRepository;
    private final ITestimonialRepository testimonialRepository;

    @GetMapping("/offers")
    public List<Offer> getAllOffers () {
        return offerRepository.findAll();
    }

    @GetMapping("/testimonials")
    public List<TestimonialInfoDTO> getAllTestimonials () {
        List<Testimonial> testimonials = testimonialRepository.findAll();
        return testimonials.stream().map(testimonial -> new TestimonialInfoDTO(
                testimonial.getId(),
                testimonial.getContent(),
                testimonial.getImg(),
                testimonial.getAccount().getId(),
                testimonial.getAccount().getFullName()
        )).collect(Collectors.toList());
    }
}
