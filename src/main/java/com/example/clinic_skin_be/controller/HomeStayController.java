package com.example.clinic_skin_be.controller;

import com.example.clinic_skin_be.model.City;
import com.example.clinic_skin_be.model.HomeStay;

import com.example.clinic_skin_be.service.CityService;
import com.example.clinic_skin_be.service.HomeStayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("")
public class HomeStayController {

    private final HomeStayService homeStayService;
    private final CityService cityService;

    // Hiển thị danh sách homestay
    @GetMapping("")
    public String homeStayList(Model model) {
        List<HomeStay> homeStayList = homeStayService.list();
        model.addAttribute("homeStayList", homeStayList);
        return "list";
    }
    @GetMapping("/detail/{id}")
    public String homeStayDetail(@PathVariable Long id, Model model) {
        HomeStay homeStay = homeStayService.getHomeStay(id);
        model.addAttribute("homeStay", homeStay);
        return "detail";
    }

    @GetMapping("/add")
    public String homeStayAdd(Model model) {
        HomeStay homeStay = new HomeStay();
        List<City> cityList = cityService.cityList();

        model.addAttribute("homeStay", homeStay);
        model.addAttribute("cityList", cityList);
        return "update";
    }

    // Mở form sửa homestay
    @GetMapping("/edit/{id}")
    public String editHomeStay(@PathVariable Long id, Model model) {
        HomeStay homeStay = homeStayService.getHomeStay(id);
        List<City> cityList = cityService.cityList();

        model.addAttribute("homeStay", homeStay);
        model.addAttribute("cityList", cityList);

        return "update";
    }

    // Cập nhật homestay sau khi submit form
    @PostMapping("/update")
    public String updateHomeStay(@ModelAttribute("homeStay") HomeStay homeStay) {
        homeStayService.save(homeStay);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteHomeStay(@PathVariable Long id) {
        homeStayService.deleteById(id);
        return "redirect:/";
    }

}
