package com.abdelrahman.appointmentscheduling.dto;

import java.util.Set;

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
@Schema(name = "Doctor")
public class DoctorDto {

	@Schema(accessMode =AccessMode.READ_ONLY)
	private Integer id;
	
	private String name;
	
	private String specialization;
	@Schema(accessMode = AccessMode.READ_ONLY)
	private Set<AppointmentDto> slots;
}
