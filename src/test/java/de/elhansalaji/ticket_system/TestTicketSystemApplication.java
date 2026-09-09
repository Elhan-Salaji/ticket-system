package de.elhansalaji.ticket_system;

import org.springframework.boot.SpringApplication;

public class TestTicketSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
