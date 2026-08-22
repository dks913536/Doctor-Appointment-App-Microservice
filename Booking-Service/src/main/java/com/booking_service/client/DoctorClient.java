package com.booking_service.client;

import com.booking_service.dto.Doctor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Doctor-Service",url = "http://localhost:8081/api/v1/doctor")
public interface DoctorClient {

    @GetMapping("/getDoctorById")
    public Doctor getDoctorById(@RequestParam long id);
}
