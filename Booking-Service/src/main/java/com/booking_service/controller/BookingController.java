package com.booking_service.controller;

import com.booking_service.client.DoctorClient;
import com.booking_service.client.PatientClient;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@GetMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private DoctorClient doctorClient;

    @Autowired
    private PatientClient patientClient;
}
