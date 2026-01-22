package com.abdelrahman.appointmentscheduling.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.abdelrahman.appointmentscheduling.dto.DoctorDto;
import com.abdelrahman.appointmentscheduling.entities.Doctor;

@Mapper(componentModel = "spring",uses =AppointmentMapper.class)
public interface DoctorMapper {

	DoctorDto toDto(Doctor doctor);
	
	Doctor toEntity(DoctorDto dto);
	
	List<DoctorDto> toListDto(List<Doctor> doctors);
}
