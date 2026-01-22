package com.abdelrahman.appointmentscheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdelrahman.appointmentscheduling.entities.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

//	@Query("UPDATE Booking b SET b.status='CANCELED' WHERE b.patientName=:patientName OR b.id=:bookingId")
//	@Modifying
//	void cancelBooking(
//			@Param("patientName") String patientName 
//			,@Param("bookingId") Integer bookingId
//			);
}
