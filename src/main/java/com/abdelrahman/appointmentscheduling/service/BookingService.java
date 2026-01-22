package com.abdelrahman.appointmentscheduling.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.entities.Booking;
import com.abdelrahman.appointmentscheduling.enums.AppointmentStatus;
import com.abdelrahman.appointmentscheduling.enums.BookingStatus;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.repository.BookingRepo;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

	@Autowired
	private BookingRepo bookingRepo;
	@Autowired
	private AppointmentService slotService;
	@Transactional
	public Booking book(Booking booking) {
		// لازم نجيبه من ال database الاول 
		AppointmentSlot slot = slotService.findById(booking.getSlot().getId());	
		// التأكد من حالة ال status
		if(!slot.getStatus().equals(AppointmentStatus.AVAILABLE)) {
			throw new RuntimeException("Slot is not avilable");
		}
		
	    slotService.bookStatus(booking.getSlot().getId());
		booking.setStatus(BookingStatus.CONFIRMED);		
		return bookingRepo.save(booking);
	}
	
	public Booking update(Integer id,Booking booking) {
		Optional<Booking> bookOp = bookingRepo.findById(id);
		if(bookOp.isPresent()) {
			return bookingRepo.save(booking);
		}
		else {
			throw new RecordNotFoundException("This Booking is not found") ; // until handling exception
		}
	}
	
	public List<Booking> findAll(){
		return bookingRepo.findAll();
	}
	
	public Booking findById(Integer id){
		return bookingRepo.findById(id).orElseThrow(()->new RecordNotFoundException("This Booking not found")); // until handling or else throw not found exception
	}
	@Transactional
	public void cancelBook(Integer id) {
		Booking book = bookingRepo.findById(id)
	            .orElseThrow(() -> new RecordNotFoundException("Booking not found"));
		
		if(book.getStatus().equals(BookingStatus.CANCELED)) {
			throw new RuntimeException("Booking canceled already");
		}
		
		book.setStatus(BookingStatus.CANCELED);
		book.getSlot().setStatus(AppointmentStatus.AVAILABLE);
	}
	
}
