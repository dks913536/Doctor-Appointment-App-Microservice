package com.doctor_service.controller;


import com.doctor_service.entity.Doctor;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private S3Service s3Service;

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

    // S3 part

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file) throws Exception{
        String imageUrl=s3Service.uploadFile(file);

        return ResponseEntity.ok(imageUrl);
    }

    
}
