package com.abdelrahman.appointmentscheduling.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.abdelrahman.appointmentscheduling.dto.AppointmentDto;
import com.abdelrahman.appointmentscheduling.dto.BookingDto;
import com.abdelrahman.appointmentscheduling.entities.AppointmentSlot;
import com.abdelrahman.appointmentscheduling.entities.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

	@Mapping(source = "slot.id", target = "slotId")
	BookingDto toDto(Booking booking);
	// to handle dto.slotId to entity 
	// this in controller 
	@Mapping(target = "slot", source = "slotId")
	Booking toEntity(BookingDto dto);
	
	List<BookingDto> toListDto(List<Booking> list);

	@AfterMapping
	default void setSlotIdToDto(Booking booking,@MappingTarget BookingDto dto) {
		if (booking.getSlot() != null) {
	        dto.setSlotId(booking.getSlot().getId());
	    }	
	}
	// don't need @AfterMapping 
	default AppointmentSlot mapIdToSlot(Integer slotId) {
	    if (slotId == null) return null;
	    AppointmentSlot slot = new AppointmentSlot();
	    slot.setId(slotId);
	    return slot;
	}
	
}
