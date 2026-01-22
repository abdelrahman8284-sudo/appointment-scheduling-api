package com.abdelrahman.appointmentscheduling.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.abdelrahman.appointmentscheduling.repository.AppointmentRepo;

@Service
public class AppointmentScheduler {

	@Autowired
	private AppointmentRepo slotRepo;
	
	//private List<AppointmentSlot> slots = slotService.findAll();
 
	@Scheduled(fixedRate = 60000)
	public void schedulAppointmentStatus() {
		slotRepo.expireSlot(LocalDateTime.now());
	}
}
