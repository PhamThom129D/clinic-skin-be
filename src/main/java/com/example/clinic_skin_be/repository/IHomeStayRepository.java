package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.HomeStay;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHomeStayRepository extends JpaRepository<HomeStay,Long> {
}
