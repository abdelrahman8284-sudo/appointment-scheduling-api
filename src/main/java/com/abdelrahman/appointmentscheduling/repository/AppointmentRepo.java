package com.abdelrahman.appointmentscheduling.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepo extends JpaRepository<AppointmentSlot, Integer>,JpaSpecificationExecutor<AppointmentSlot> {

	@Query("UPDATE AppointmentSlot a SET a.status='BOOKED' WHERE a.id =:id")
	@Modifying
	void bookSlot(@Param("id") Integer slotId);
	
	@Query("SELECT COUNT(a) FROM AppointmentSlot a WHERE a.doctor.id = :doctorId " +
		       "AND (:currentSlotId IS NULL OR a.id <> :currentSlotId) " + // عشان لو بعمل update تأكد اني شغال على ال record نفسه
			
		       "AND :newStart < a.endTime " +
		       "AND :newEnd > a.startTime")
	long findSizeOfConflictingAppointments(
			@Param("doctorId") Integer doctorId
			,@Param("currentSlotId") Integer currentSlotId
			,@Param("newStart") LocalDateTime newStart
			,@Param("newEnd") LocalDateTime newEnd);
	
	@Query("UPDATE  AppointmentSlot a SET a.status ='EXPIRED' WHERE a.status ='AVAILABLE' OR a.status  AND a.endTime< :now")
	@Modifying
	@Transactional
	void expireSlot(LocalDateTime now);
	@Override
	@EntityGraph(attributePaths = "doctor")
	List<AppointmentSlot> findAll(Specification<AppointmentSlot> spec);
}
 