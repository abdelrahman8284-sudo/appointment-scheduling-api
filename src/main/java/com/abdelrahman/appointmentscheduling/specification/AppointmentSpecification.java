package com.abdelrahman.appointmentscheduling.specification;

import org.springframework.data.jpa.domain.Specification;

import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.enums.AppointmentStatus;


public class AppointmentSpecification {

	public static Specification<AppointmentSlot> availablesWithDoctorId(Integer doctorId){
		return (root, query, cb) ->{
			
			//root.fetch("doctor",JoinType.LEFT);
			
			if(doctorId==null)
					cb.conjunction();
					
			return cb.equal(root.join("doctor").get("id"),doctorId);			
		};
	}
	public static Specification<AppointmentSlot> availableSlots(){
		return (root, query, cb) ->cb.equal(root.get("status"),AppointmentStatus.AVAILABLE); 
	}
	
	public static Specification<AppointmentSlot> hasSpecialization(String spec){
		return (root, query, cb) ->{
			if(spec==null)
					cb.conjunction();			
			return cb.equal(root.join("doctor").get("specialization"),spec);		
		};
	}
}
