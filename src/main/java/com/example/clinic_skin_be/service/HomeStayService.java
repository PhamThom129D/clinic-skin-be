package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.HomeStay;
import com.example.clinic_skin_be.repository.ICityRepository;
import com.example.clinic_skin_be.repository.IHomeStayRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeStayService {
    private final ICityRepository cityRepository;
    private final IHomeStayRepository homeStayRepository;

    public List<HomeStay> list(){
        return homeStayRepository.findAll();
    }

    public HomeStay getHomeStay(Long homeStayId){
        return homeStayRepository.findById(homeStayId).orElse(null);
    }

    public HomeStay save(HomeStay homeStay){
        return homeStayRepository.save(homeStay);
    }

    public void deleteById(Long id){
        homeStayRepository.deleteById(id);
    }

    public List<HomeStay> searchByCityName(String cityName){
        return null;
    }
}
