package com.abdelrahman.appointmentscheduling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.entities.Booking;
import com.abdelrahman.appointmentscheduling.entities.Doctor;
import com.abdelrahman.appointmentscheduling.enums.AppointmentStatus;
import com.abdelrahman.appointmentscheduling.enums.BookingStatus;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.repository.BookingRepo;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

	@Mock
	private BookingRepo bookingRepo;
	@Mock
	private AppointmentService slotService;
	@InjectMocks
	private BookingService bookingService;
	
	private LocalDateTime start;
	
	private LocalDateTime end;
	
	private Doctor doctor;
		
	private AppointmentSlot slot;
	
	private Booking booking;

	@BeforeEach
	void setUp() {
		this.start = LocalDateTime.now().plusHours(1);
		this.end = start.plusHours(1);
		
		this.doctor = Doctor.builder().id(1).name("TestName").specialization("TestSpectialization").active(true).build();

		this.slot = new AppointmentSlot(
				1
				, AppointmentStatus.AVAILABLE
				,start
				,end,this.doctor
				); 
		this.booking = Booking.builder()
				.id(1)
				.patientName("TestPatient")
				.bookingTime(LocalDateTime.now())
				.slot(slot)
				.build();
	}
	
	@Nested
	class CreateBooking{
		@Test
		@DisplayName("Create Booking successfully")
		void shouldCreateBookingSuccessfully() {
			when(slotService.findById(booking.getSlot().getId())).thenReturn(slot);
			when(bookingRepo.save(any())).thenReturn(booking);
			
			Booking currentBooking = bookingService.book(booking);
			assertNotNull(currentBooking);
			assertEquals(currentBooking, booking);
			assertEquals(currentBooking.getStatus(),BookingStatus.CONFIRMED);
			
			verify(slotService).findById(booking.getSlot().getId());
			verify(slotService).bookStatus(booking.getSlot().getId());
			verify(bookingRepo).save(any());
		}
		
		@Test
		@DisplayName("Throw RecordNotFoundException if slot not found")
		void shouldThrowExceptionWhenSlotNotFound() {
			when(slotService.findById(booking.getSlot().getId())).thenThrow(new RecordNotFoundException("This Slot not found"));
			
			RecordNotFoundException exception =
					assertThrows(RecordNotFoundException.class
							, ()->bookingService.book(booking));
			assertEquals("This Slot not found",exception.getMessage());
			
			verify(slotService,never()).bookStatus(any());
			verifyNoInteractions(bookingRepo);
		}
		
		@Test
		@DisplayName("Throw RuntimeException if slot is not available")
		void shouldThrowWhenSlotIsNotAvailable() {
			slot.setStatus(AppointmentStatus.BOOKED);

			when(slotService.findById(booking.getSlot().getId())).thenReturn(slot);			
			RuntimeException exception =
					assertThrows(RuntimeException.class
							, ()->bookingService.book(booking));
			assertEquals("Slot is not avilable",exception.getMessage());
			
			verify(slotService,never()).bookStatus(any());
			verifyNoInteractions(bookingRepo);
		}	
	}
	
}
