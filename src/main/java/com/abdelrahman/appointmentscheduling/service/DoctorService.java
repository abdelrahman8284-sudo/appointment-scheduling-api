package com.abdelrahman.appointmentscheduling.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abdelrahman.appointmentscheduling.entities.Doctor;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.repository.DoctorRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

	private final DoctorRepo docRepo;
	
	public Doctor insert(Doctor doctor) {
		doctor.setActive(true);
		return docRepo.save(doctor);
	}
	
	public Doctor update(Integer id,Doctor doctor) {
		Doctor currentDoctor = docRepo.findById(id).orElseThrow(()->new RecordNotFoundException("This Doctor not found !"));
		currentDoctor.setActive(true);
		currentDoctor.setName(doctor.getName());
		currentDoctor.setSpecialization(doctor.getSpecialization());
		
		return docRepo.save(currentDoctor);
	}
	
	public List<Doctor> findAll(){
		return docRepo.findAll();
	}
	
	public Doctor findById(Integer id){
		return docRepo.findById(id).orElseThrow(()-> new RecordNotFoundException("This Doctor not found")); 
	}

}
