package com.abdelrahman.appointmentscheduling.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abdelrahman.appointmentscheduling.entities.Doctor;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.repository.DoctorRepo;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepo docRepo;
	
	public Doctor insert(Doctor doctor) {
		doctor.setActive(true);
		return docRepo.save(doctor);
	}
	
	public Doctor update(Integer id,Doctor doctor) {
		Optional<Doctor> doc = docRepo.findById(id);
		if(doc.isPresent()) {
			return docRepo.save(doctor);
		}
		else {
			throw new RecordNotFoundException("This Doctor not found !") ; // until handling exception
		}
	}
	
	public List<Doctor> findAll(){
		return docRepo.findAll();
	}
	
	public Doctor findById(Integer id){
		return docRepo.findById(id).orElseThrow(()-> new RecordNotFoundException("This Doctor not found")); 
	}

}
