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
@Transactional
public class AppointmentService {

	@Autowired
	private AppointmentRepo slotRepo;
	
	public AppointmentSlot insert(AppointmentSlot slot) {
		
		validateTiming(slot);
		slot.setStatus(AppointmentStatus.AVAILABLE);		
		
		return slotRepo.save(slot);
	}
	
	public AppointmentSlot update(Integer id, AppointmentSlot slot) {

	    AppointmentSlot existing = slotRepo.findById(id)
	        .orElseThrow(() -> new RecordNotFoundException("Slot not found"));

	    // ممنوع تعديل slot لو already booked أو expired
	    if (existing.getStatus() == AppointmentStatus.BOOKED ||
	        existing.getStatus() == AppointmentStatus.EXPIRED) {
	        throw new RuntimeException("Cannot modify a booked or expired slot");
	    }

	    existing.setStartTime(slot.getStartTime());
	    existing.setEndTime(slot.getEndTime());
	    existing.setDoctor(slot.getDoctor());

	    validateTiming(existing);

	    return slotRepo.save(existing);
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
	
	private void validateTiming(AppointmentSlot slot) {

	    if (slot.getStartTime() == null || slot.getEndTime() == null) {
	        throw new RuntimeException("Start time and end time must not be null");
	    }

	    if (slot.getDoctor() == null) {
	        throw new RuntimeException("Doctor must not be null");
	    }

	    if (slot.getEndTime().isBefore(slot.getStartTime())
	            || slot.getEndTime().isEqual(slot.getStartTime())
	            || slot.getStartTime().isBefore(LocalDateTime.now())) {

	        throw new TimeNotValidException(
	                "endTime should be after startTime and startTime should be after now"
	        );
	    }

	    boolean hasConflict = slotRepo.findSizeOfConflictingAppointments(
	            slot.getDoctor().getId(),
	            slot.getId(),
	            slot.getStartTime(),
	            slot.getEndTime() 
	    ) > 0;

	    if (hasConflict) {
	        throw new TimeNotValidException(
	                "This doctor already has an appointment during this period"
	        );
	    }
	}


	
	public List<AppointmentSlot> searchSlots (Integer doctorId,String specialization){
		Specification<AppointmentSlot> spec = 
			    Specification.where(AppointmentSpecification.availableSlots());

		if (doctorId != null) {
		    spec = spec.and(AppointmentSpecification.availablesWithDoctorId(doctorId));
		}

		if (specialization != null) {
		    spec = spec.and(AppointmentSpecification.hasSpecialization(specialization));
		}


		return slotRepo.findAll(spec);
	}
	
}
