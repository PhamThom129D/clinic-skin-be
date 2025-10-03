package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICityRepository extends JpaRepository<City,Long> {

}
