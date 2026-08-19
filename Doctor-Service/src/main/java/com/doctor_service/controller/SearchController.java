package com.doctor_service.controller;

import com.doctor_service.dto.SearchResultDto;
import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorAppointmentSchedule;
import com.doctor_service.entity.TimeSlots;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.repository.TimeSlotsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
public class SearchController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TimeSlotsRepository timeSlotsRepository;

    // Example:
    // http://localhost:8081/api/v1/doctor/search?specialization=cardiologist&areaName=btm
    @GetMapping("/search")
    public ResponseEntity<List<SearchResultDto>> searchDoctors(
                                                                @RequestParam String specialization,
                                                                @RequestParam String areaName
                                                        )
    {
        // Used to check whether appointment schedule is today or future
        LocalDate today=LocalDate.now(); // current date need to fetch for doctors

        List<SearchResultDto> result=new ArrayList<>(); //final result : response send back to SearchResultDto

        // Search doctors from database, based on specialization and area
        List<Doctor> doctors=doctorRepository.findByspecializationAndArea(specialization, areaName);

        // Process each doctor one by one
        for(Doctor doctor:doctors){
            // new DTO for each doctor // This DTO will be sent in the API response
            SearchResultDto dto=new SearchResultDto();

            List<LocalDate> validDates=new ArrayList<>(); //today+future dates
            List<LocalTime> allTimeSlots=new ArrayList<>(); //present +future date :DoctorAppointmentSchedule

            // Get all appointment schedules of the current doctor
            List<DoctorAppointmentSchedule> schedules=doctor.getAppointmentSchedules();

            // Process each appointment schedule
            for(DoctorAppointmentSchedule schedule:schedules){
                // Get the date of this appointment schedule
                LocalDate scheduleDate=schedule.getDate();

                // Get current time // Used when schedule date is today
                LocalTime now=LocalTime.now();

                // Get all time slots for this particular schedule // schedule.getId() is used to find the slots
                List<TimeSlots> timeSlots=timeSlotsRepository.getAllTimeSlots(schedule.getId());

                // Process each time slot
                for(TimeSlots ts:timeSlots){
                    // Get the time of current slot
                    LocalTime slotTime=ts.getTime();

                    // If schedule is today → only future times
                    if(scheduleDate.isEqual(today)){
                        if(slotTime.isAfter(now)){
                            allTimeSlots.add(slotTime); // Add future time slot to the result
                        }
                    }
                    // If schedule is in the future → add all times
                    else if (scheduleDate.isAfter(today)) {
                        allTimeSlots.add(slotTime);// For future dates, // all time slots can be added
                    }
                }
            }
            //  Fiil doctor info
            dto.setDoctorId(doctor.getId());
            dto.setName(doctor.getName());
            dto.setArea(doctor.getArea().getName());
            dto.setCity(doctor.getCity().getName());
            dto.setQualification(doctor.getQualification());
            dto.setSpecialization(doctor.getSpecialization());
            dto.setDates(validDates);
            dto.setTimeSlots(allTimeSlots);

            result.add(dto);    // Add this doctor's DTO to final result list

        }
        // Return the final doctor list as HTTP 200 OK response
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // http://localhost:80802/api/v1/doctor/getdoctorbyid?id=1
    @GetMapping("/getDoctorById")
    public Doctor getDoctorById(@RequestParam long id){
        return  doctorRepository.findById(id).get();
    }
}
