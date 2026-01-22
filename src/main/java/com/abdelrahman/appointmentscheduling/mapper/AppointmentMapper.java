package com.abdelrahman.appointmentscheduling.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.abdelrahman.appointmentscheduling.dto.AppointmentDto;
import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
	@Mapping(source="doctor.id",target="doctorId")
	AppointmentDto toDto(AppointmentSlot slot);

	@Mapping(target="doctor.id",source = "doctorId")
	AppointmentSlot toEntity(AppointmentDto dto);
	
	List<AppointmentDto> toListDto(List<AppointmentSlot> slots);

	
	@AfterMapping
	default void handleStatus(AppointmentSlot slot, @MappingTarget AppointmentDto dto) {
	    if (slot.getStatus() != null) {
	        dto.setStatus(slot.getStatus().name().toUpperCase());
	    }
	}
}
