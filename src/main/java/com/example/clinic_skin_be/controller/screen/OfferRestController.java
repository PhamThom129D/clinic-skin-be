package com.example.clinic_skin_be.controller.screen;

import com.example.clinic_skin_be.model.screen.Offer;
import com.example.clinic_skin_be.repository.screen.IOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@AllArgsConstructor
public class OfferRestController {
    @Autowired
    private IOfferRepository offerRepository;

    @GetMapping()
    public List<Offer> getAllOffers () {
        return offerRepository.findAll();
    }
}
