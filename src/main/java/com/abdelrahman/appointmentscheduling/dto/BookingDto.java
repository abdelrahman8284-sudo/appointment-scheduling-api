package com.abdelrahman.appointmentscheduling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="Booking")
public class BookingDto {

	@Schema(accessMode =AccessMode.READ_ONLY)
	private Integer id;
	
	private String status;
	
	private String patientName;
	
	private Integer slotId;
}
