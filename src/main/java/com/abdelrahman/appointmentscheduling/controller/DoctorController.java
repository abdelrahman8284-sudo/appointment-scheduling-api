package com.abdelrahman.appointmentscheduling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdelrahman.appointmentscheduling.dto.DoctorDto;
import com.abdelrahman.appointmentscheduling.entities.Doctor;
import com.abdelrahman.appointmentscheduling.mapper.DoctorMapper;
import com.abdelrahman.appointmentscheduling.service.DoctorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/doctor")
@Tag(name="Doctor")
public class DoctorController {

	@Autowired
	private DoctorService docService;
	
	@Autowired
	private DoctorMapper mapper ;
	
	@Operation(summary ="Insert new Doctor")
	@PostMapping
	public ResponseEntity<?> insert(@RequestBody DoctorDto dto){
		Doctor doc = docService.insert(mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(doc));
	}
	@Operation(summary = "Update doctor")
	@PutMapping("/id/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody DoctorDto dto){
		Doctor doc = docService.update(id, mapper.toEntity(dto));
		return ResponseEntity.ok(mapper.toDto(doc));
	}
	@Operation(summary ="Find All Doctors")
	@GetMapping
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok(mapper.toListDto(docService.findAll()));
	}
	@Operation(summary ="Find Doctor by id")
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id){
		return ResponseEntity.ok(mapper.toDto(docService.findById(id)));
	}
}
