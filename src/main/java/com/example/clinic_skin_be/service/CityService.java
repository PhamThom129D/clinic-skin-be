package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.City;
import com.example.clinic_skin_be.repository.ICityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityService {
    private final ICityRepository cityRepository;

    public List<City> cityList()
    {
        return cityRepository.findAll();
    }

    public  City cityById(Long id)
    {
        return cityRepository.findById(id).orElse(null);
    }

}
