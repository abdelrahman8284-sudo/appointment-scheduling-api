package com.abdelrahman.appointmentscheduling.controller;

import java.util.List;

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

import com.abdelrahman.appointmentscheduling.dto.AppointmentDto;
import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.mapper.AppointmentMapper;
import com.abdelrahman.appointmentscheduling.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/slot")
@Tag(name = "Appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService slotService;
	
	@Autowired
	private AppointmentMapper mapper;
	
	@Operation(summary = "Insert new slot",description = "")
	@PostMapping
	public ResponseEntity<?> insert(
			@RequestBody AppointmentDto dto){
		AppointmentSlot slot = slotService.insert(mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(slot));
	}
	@Operation(summary ="Update slot")
	@PutMapping("/id/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody AppointmentDto dto){
		AppointmentSlot slot = slotService.update(id, mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(slot));
	}
	@Operation(summary ="Find all Slots")
	@GetMapping
	public ResponseEntity<?> findAll(){
		List<AppointmentDto> slots = mapper.toListDto(slotService.findAll());
		return ResponseEntity.ok(slots);
	}
	@Operation(summary ="Find by slot id")
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id){
		AppointmentSlot slot = slotService.findById(id);
		
		return ResponseEntity.ok(mapper.toDto(slot));
	}
	@Operation(summary = "Search available slots",description = "To search about availables slots by doctor id or his specialization")
	@GetMapping("/search")
	public ResponseEntity<?> search(
			@RequestParam(required = false) Integer doctorId,
			@RequestParam(required = false) String specialization){
		List<AppointmentSlot> slots = slotService.searchSlots(doctorId, specialization);
		return ResponseEntity.ok(mapper.toListDto(slots));
	}
	
}
