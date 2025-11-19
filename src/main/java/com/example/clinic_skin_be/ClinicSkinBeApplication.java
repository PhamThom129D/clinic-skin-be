package com.example.clinic_skin_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
public class ClinicSkinBeApplication {

    public static void main(String[] args) {

//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SpringApplication.run(ClinicSkinBeApplication.class, args);
    }

}

