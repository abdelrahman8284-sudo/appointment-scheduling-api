package com.abdelrahman.appointmentscheduling.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition()
public class OpenApiConfiguration {
	
	private static final String SECURITY_SCHEME_NAME = "Bearer oAuth Token";
	@Bean
	public OpenAPI customOpenApi(
			@Value("${application.description}") String appDescription
		    ,@Value("${application.version}") String appVersion) {
		return new OpenAPI()
				.info(new Info().title("Appointment Scheduling API").description(appDescription).version(appVersion).contact(getContact())
						.termsOfService("http://swagger.io/terms/").license(getLicense()))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME, Arrays.asList("read", "write")))
				.components(
						new Components()
						.addSecuritySchemes(
						SECURITY_SCHEME_NAME
						,new SecurityScheme().name("Bearer oAuth Token")
						.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

	private License getLicense() {
		License license = new License();
		license.setName("Apache License , Version 2.0");
		license.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
		license.setExtensions(Collections.emptyMap());
		return license;
	}
	
	 private Contact getContact() {
	    Contact contact = new Contact();
	    contact.setEmail("info@gmail.com"); // As an experiment
	    contact.setName("appointment-scheduling");
	    contact.setUrl("https://www.book.com");
	    contact.setExtensions(Collections.emptyMap());
	    return contact;
	 }
}
