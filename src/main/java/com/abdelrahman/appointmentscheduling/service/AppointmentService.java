package com.abdelrahman.appointmentscheduling.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.enums.AppointmentStatus;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.exception.TimeNotValidException;
import com.abdelrahman.appointmentscheduling.repository.AppointmentRepo;
import com.abdelrahman.appointmentscheduling.specification.AppointmentSpecification;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepo slotRepo;
	
	public AppointmentSlot insert(AppointmentSlot slot) {
		
		if(timmingIsValid(slot)){
			slot.setStatus(AppointmentStatus.AVAILABLE);		
		}
		return slotRepo.save(slot);
	}
	
	public AppointmentSlot update(Integer id,AppointmentSlot slot) {
		AppointmentSlot currentSlot = null ;
		Optional<AppointmentSlot> slotOp = slotRepo.findById(id);
		if(slotOp.isPresent()) {				
			if(timmingIsValid(slot)) {	
				currentSlot = slotRepo.save(slot);
			}
		}
		else {
			throw new RecordNotFoundException("Slot not found") ; // until handling exception
		}
		return currentSlot;
	}
	
	public List<AppointmentSlot> findAll(){
		return slotRepo.findAll();
	}
	
	public AppointmentSlot findById(Integer id){
		return slotRepo.findById(id).orElseThrow(()->new RecordNotFoundException("This Slot not found"));
	}
	@Transactional
	public void bookStatus(Integer slotId) {
		slotRepo.bookSlot(slotId);
	}
	
	private boolean timmingIsValid(AppointmentSlot slot) throws RuntimeException {
		boolean isValid = true;
		
		boolean hasConfilict = slotRepo.findSizeOfConflictingAppointments(
				slot.getDoctor().getId()
				,slot.getId()
				,slot.getStartTime()
				,slot.getEndTime())>0;
				
		if(slot.getEndTime().isBefore(slot.getStartTime())|| slot.getEndTime().isEqual(slot.getStartTime()) || slot.getStartTime().isBefore(LocalDateTime.now())) {
			isValid =false;
			throw new TimeNotValidException("endTime should be after startTime or startTime should be after now");
		} 
		if(hasConfilict) {
			isValid = false;
			throw new TimeNotValidException("This doctor already has an appointment during this period");
		}

		return isValid;
	}
	
	public List<AppointmentSlot> searchSlots (Integer doctorId,String specialization){
		Specification<AppointmentSlot> spec =Specification.where(AppointmentSpecification.availableSlots());
		
		if(doctorId!=null) {
			spec = Specification.where(AppointmentSpecification.availablesWithDoctorId(doctorId));
		}
		
		if(specialization!=null) {
			spec = Specification.where(AppointmentSpecification.hasSpecialization(specialization));
		}
		return slotRepo.findAll(spec);
	}
}
