package com.abdelrahman.appointmentscheduling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.entities.Doctor;
import com.abdelrahman.appointmentscheduling.enums.AppointmentStatus;
import com.abdelrahman.appointmentscheduling.exception.RecordNotFoundException;
import com.abdelrahman.appointmentscheduling.exception.TimeNotValidException;
import com.abdelrahman.appointmentscheduling.repository.AppointmentRepo;
import com.abdelrahman.appointmentscheduling.service.AppointmentService;

@ExtendWith(MockitoExtension.class)
class AppointmentSlotTest {
	@Mock
	private AppointmentRepo appointmentRepo;
	@InjectMocks
	private AppointmentService appointmentService; 
	
	private AppointmentSlot slot;
	
	private AppointmentSlot slotToUpdate;
	
	private Doctor doctor;
	
	LocalDateTime start ;
	
	LocalDateTime end ;
	
	@BeforeEach
	void setUp() {
		// عشان نخليش الوقت ثابت عشان ال test يبقى صح 
		this.start = LocalDateTime.now().plusHours(1);
		this.end = start.plusHours(1);
		
		this.doctor = Doctor.builder().id(1).name("TestName").specialization("TestSpectialization").active(true).build();

		this.slot = new AppointmentSlot(
				1
				, AppointmentStatus.AVAILABLE
				,start
				,end,this.doctor
				); 
		
		this.slotToUpdate = AppointmentSlot.builder()
				.id(1)
				.doctor(doctor)
				.startTime(start.plusHours(1))
				.endTime(end.plusHours(1))
				.status(AppointmentStatus.BOOKED)
				.build();
	}
	
	@Nested
	class CreateSlotTests{
		@Test
		@DisplayName("Insert Successfully test")
		void shouldInsertSuccessfully() {
			// Given
			when(appointmentRepo.findSizeOfConflictingAppointments(
			        any(), any(), any(), any()
			)).thenReturn(0L);

			when(appointmentRepo.save(any())).thenReturn(slot);
			
			// when
			AppointmentSlot currentSlot = appointmentService.insert(slot);
			assertNotNull(currentSlot);
			assertEquals(currentSlot,slot);			
			// then
			verify(appointmentRepo).findSizeOfConflictingAppointments(
			        slot.getDoctor().getId(),
			        slot.getId(),
			        slot.getStartTime(),
			        slot.getEndTime()
			);
			

			verify(appointmentRepo).save(slot);
		}
		
		@Test
		@DisplayName("Should throw new TimeNotValid Exception when EndTime Before start time")
		void shouldThrowExceptionWhenEndTimeBefore() {
			// المفروض الاول نخلي ال endTime before
			// مش المفروض نكلم دالة findSizeOfConflictingAppointments
			// المفروض بنختبر الوقت فقط وليس ال slotConfilict
			
			// Given 
			slot.setEndTime(slot.getStartTime().minusMinutes(1));
			
			TimeNotValidException exception =
					assertThrows(TimeNotValidException.class, ()->appointmentService.insert(slot));
			
			assertEquals("endTime should be after startTime and startTime should be after now", exception.getMessage());
			verifyNoInteractions(appointmentRepo);
		}
		
		@Test
		@DisplayName("Should throw new TimeNotValidException when endTime = startTime")
		void shouldThrowExceptionWhenEndTimeEqualsStart() {
			// نفس منطق الدالة الاخيرة
			
			// Given 
			slot.setEndTime(slot.getStartTime());
			
			TimeNotValidException exception =
					assertThrows(TimeNotValidException.class, ()->appointmentService.insert(slot));
			
			assertEquals("endTime should be after startTime and startTime should be after now", exception.getMessage());
			verifyNoInteractions(appointmentRepo);
		}
		
		@Test
		@DisplayName("Should throw new TimeNotValidException when starttime before now")
		void shouldThrowExceptionWhenStartTimeBeforeNow() {
			// نفس منطق الدالة الاخيرة

			
			// Given 
			slot.setStartTime(LocalDateTime.now().minusMinutes(1));
			
			TimeNotValidException exception =
					assertThrows(TimeNotValidException.class, ()->appointmentService.insert(slot));
			
			assertEquals("endTime should be after startTime and startTime should be after now", exception.getMessage());
			verifyNoInteractions(appointmentRepo);
		}
		
		@Test
		@DisplayName("Should throw new TimeNotValidException when confilict happens")
		void shouldThrowExceptionWhenConfilictHappens() {

			// هنا يحصل exception لو جيت اضيف slot جديد لنفس الدكنور في نفس المعاد او في معاد يسبب conflict 
			
			// Given 
			AppointmentSlot currentSlot =AppointmentSlot
					.builder()
					.doctor(doctor)
					.startTime(start.plusMinutes(30L))
					.endTime(end)
					.build();
			
			when(appointmentRepo.findSizeOfConflictingAppointments(
					currentSlot.getDoctor().getId()
					, currentSlot.getId()
					,currentSlot.getStartTime()
					,currentSlot.getEndTime())).thenReturn(1L);
			
			
			TimeNotValidException exception =
					assertThrows(TimeNotValidException.class, ()->appointmentService.insert(currentSlot));
			
			assertEquals("This doctor already has an appointment during this period", exception.getMessage());
			verify(appointmentRepo,never()).save(any());
			verify(appointmentRepo,times(1)).findSizeOfConflictingAppointments(any(), any(), any(), any());
		}
		
		
		
	}
	@Nested
	class UpdateSlotTests{
		
		@Test
		@DisplayName("Update Successfully Tests")
		void shouldUpdateSuccessfully() {
			// Given
			Integer id = 1;
			when(appointmentRepo.findById(id)).thenReturn(Optional.of(slot));
			when(appointmentRepo.save(any())).thenReturn(slotToUpdate);
			when(appointmentRepo.findSizeOfConflictingAppointments(
			        any(), any(), any(), any()
			)).thenReturn(0L);
			// when
			AppointmentSlot updatedSlot = appointmentService.update(id, slotToUpdate);
			assertNotNull(updatedSlot);
			assertEquals(updatedSlot,slotToUpdate);
			
			verify(appointmentRepo,times(1)).findById(id);
			verify(appointmentRepo).save(any());
			verify(appointmentRepo,times(1)).findSizeOfConflictingAppointments(any(), any(), any(), any());
			
		}
		
		@Test
		@DisplayName("Should throw RecordNotFoundException when slot not found")
		void shouldThrowWhenSlotNotFound() {
			// Given 
			Integer id=100;
			when(appointmentRepo.findById(id)).thenReturn(Optional.empty());
			
			// when 
			RecordNotFoundException exception = 
					assertThrows(RecordNotFoundException.class
							,()->appointmentService.update(id,slotToUpdate));
			
			
			assertEquals("Slot not found", exception.getMessage());
			verify(appointmentRepo,never()).findSizeOfConflictingAppointments(any(),any(),any(),any());
			verify(appointmentRepo,never()).save(any());
		}
		@Test
		@DisplayName("Should throw RecordNotFoundException when slot not found")
		void shouldThrowWhenSlotIsBooked() {
			Integer id = 1;
			slot.setStatus(AppointmentStatus.BOOKED);
			when(appointmentRepo.findById(id)).thenReturn(Optional.of(slot));
			RuntimeException exception =
					assertThrows(RuntimeException.class
					,()-> appointmentService.update(id, slotToUpdate));
			
			assertEquals("Cannot modify a booked or expired slot",exception.getMessage());
			verify(appointmentRepo,never()).findSizeOfConflictingAppointments(any(),any(),any(),any());
			verify(appointmentRepo,never()).save(any());
			
		}
		@Test
		@DisplayName("Should throw RecordNotFoundException when slot is expired")
		void shouldThrowWhenSlotIsExpired() {
			Integer id = 1;
			slot.setStatus(AppointmentStatus.EXPIRED);
			when(appointmentRepo.findById(id)).thenReturn(Optional.of(slot));
			RuntimeException exception =
					assertThrows(RuntimeException.class
					,()-> appointmentService.update(id, slotToUpdate));
			
			assertEquals("Cannot modify a booked or expired slot",exception.getMessage());
			verify(appointmentRepo,never()).findSizeOfConflictingAppointments(any(),any(),any(),any());
			verify(appointmentRepo,never()).save(any());
		}
		
		// تغطية حالة واحدة فقط من حالات ال invalid timing للتأكد من ان دالة validateTiming بتتنادى
		@Test
		@DisplayName("Should throw TimeNotValidException when timming invalid")
		void shouldThrowWhenTimmingInvalid() {
			Integer id = 1 ;
			slotToUpdate.setEndTime(start.minusMinutes(5));
			when(appointmentRepo.findById(id)).thenReturn(Optional.of(slot));
			
			TimeNotValidException exception =
					assertThrows(TimeNotValidException.class
							,()->appointmentService.update(id,slotToUpdate));
			assertEquals("endTime should be after startTime and startTime should be after now", exception.getMessage());
			
			verify(appointmentRepo,never()).save(any());
			verify(appointmentRepo).findById(any());
		}
		
		@Test
		@DisplayName("Should throw TimeNotValid When conflict happens")
		void shouldThrowWhenConflictHappens(){
			Integer id = 1;
			
			when(appointmentRepo.findById(id)).thenReturn(Optional.of(slot));
			when(appointmentRepo.findSizeOfConflictingAppointments(any(),any(),any(),any())).thenReturn(1L);
		
			 TimeNotValidException exception =
					 assertThrows(TimeNotValidException.class
							 ,()->appointmentService.update(id, slotToUpdate));
					
			 assertEquals("This doctor already has an appointment during this period",exception.getMessage());
			 
			 verify(appointmentRepo,times(1)).findSizeOfConflictingAppointments(any(),any(),any(),any());
			 verify(appointmentRepo,never()).save(any());
		}
		
	}
	
	


}
