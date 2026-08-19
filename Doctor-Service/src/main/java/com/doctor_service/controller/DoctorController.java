package com.doctor_service.controller;


import com.doctor_service.entity.Doctor;
import com.doctor_service.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/create-profile")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor){
        //set bidirectional relationship
        // http://localhost:8081/api/v1/doctors/create-profile
        if(doctor.getAppointmentSchedules() !=null){
            doctor.getAppointmentSchedules().forEach(schedule ->{
                schedule.setDoctor(doctor);

                if(schedule.getTimeSlots() != null){
                    schedule.getTimeSlots().forEach(slot ->{
                        slot.setDoctorAppointmentSchedule(schedule);
                    });
                }
            });
        }
        Doctor saveDoctor=doctorRepository.save(doctor);
        return new ResponseEntity<>(saveDoctor, HttpStatus.CREATED);
    }

    
}
