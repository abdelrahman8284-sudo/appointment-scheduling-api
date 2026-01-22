package com.abdelrahman.appointmentscheduling.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@Schema(name ="Appointment Slot")
public class AppointmentDto {

	@Schema(accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	private String status;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Schema(example = "2026-01-14 11:30")
	private LocalDateTime startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Schema(example="2026-01-14 12:30")
	private LocalDateTime endTime;
	@Schema(example="5")
	private Integer doctorId;
	

	public AppointmentDto(Integer id, LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
}
