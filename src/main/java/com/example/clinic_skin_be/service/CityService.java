package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.City;
import com.example.clinic_skin_be.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAllCitiesDesc();
    }

    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public City updateCity(Long id, City cityDetails) {
        return cityRepository.findById(id).map(city -> {
            city.setThanhPho(cityDetails.getThanhPho());
            city.setQuocGia(cityDetails.getQuocGia());
            city.setDienTich(cityDetails.getDienTich());
            city.setDanSo(cityDetails.getDanSo());
            city.setGdp(cityDetails.getGdp());
            city.setMoTa(cityDetails.getMoTa());
            return cityRepository.save(city);
        }).orElseThrow(() -> new RuntimeException("City not found with id " + id));
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
