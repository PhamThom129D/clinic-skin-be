package com.example.clinic_skin_be.repository.screen;

import com.example.clinic_skin_be.model.screen.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOfferRepository extends JpaRepository<Offer, Long> {
}
