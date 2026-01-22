package com.abdelrahman.appointmentscheduling.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdelrahman.appointmentscheduling.dto.BookingDto;
import com.abdelrahman.appointmentscheduling.entities.Booking;
import com.abdelrahman.appointmentscheduling.mapper.BookingMapper;
import com.abdelrahman.appointmentscheduling.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/book")
@Tag(name="Booking")
public class BookingController {

	@Autowired
	private BookingService bService;
	
	@Autowired
	private BookingMapper mapper;
	
	@Operation(summary ="Create Booking",description = "ِAppointment must be available")
	@PostMapping
	public ResponseEntity<?> book(@RequestBody BookingDto dto){
		Booking booking = bService.book(mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(booking));
	}
	@Operation(summary ="Update Booking")
	@PutMapping("/id/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody BookingDto dto){
		Booking booking= bService.update(id,mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(booking));
	}
	@Operation(summary ="Find All Bookings")
	@GetMapping
	public ResponseEntity<?> findAll(){
		List<BookingDto> dtos = mapper.toListDto(bService.findAll());
		return ResponseEntity.ok(dtos);
	}
	@Operation(summary ="Find Booking by id")
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id){
		Booking booking = bService.findById(id);
		return ResponseEntity.ok(mapper.toDto(booking));
	}
	@Operation(summary = "Cancel Booking by Id")
	@PutMapping("/cancel")
	public void cancelBooking(@RequestParam Integer id) {
		bService.cancelBook(id);
	}
	
}
