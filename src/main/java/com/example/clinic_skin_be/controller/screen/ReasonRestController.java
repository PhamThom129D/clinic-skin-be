package com.example.clinic_skin_be.controller.screen;

import com.example.clinic_skin_be.model.screen.Offer;
import com.example.clinic_skin_be.model.screen.Reason;
import com.example.clinic_skin_be.repository.screen.IReasonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reasons")
@AllArgsConstructor
public class ReasonRestController {
    @Autowired
    private IReasonRepository reasonRepository;

    @GetMapping()
    public List<Reason> getAllOffers () {
        return reasonRepository.findAll();
    }
}
