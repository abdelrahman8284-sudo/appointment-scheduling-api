package com.abdelrahman.appointmentscheduling.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdelrahman.appointmentscheduling.entities.Doctor;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {

	@EntityGraph(attributePaths = "slots")
	//@Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.slots WHERE d.id=:id")
	Optional<Doctor> findById(Integer id);
	@EntityGraph(attributePaths = "slots")
	List<Doctor> findAll();
}
