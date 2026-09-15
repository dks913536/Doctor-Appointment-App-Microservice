package com.patient_service.controller;

import com.patient_service.entity.Patient;
import com.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {

        Patient savedPatient = patientRepository.save(patient);

        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/getpatientbyid")
    public Patient getPatientById(@RequestParam long id){
        return patientRepository.findById(id).get();
    }
}
