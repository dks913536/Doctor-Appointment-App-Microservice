package com.payment_service.client;

import com.payment_service.dto.BookingConfirmation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BOOKING-SERVICE",url = "http://localhost:9091/api/v1/booking")
public interface BookingClient {
    @GetMapping("/getBookingById")
    public BookingConfirmation getBookingById(@RequestParam long bookingId);
}
