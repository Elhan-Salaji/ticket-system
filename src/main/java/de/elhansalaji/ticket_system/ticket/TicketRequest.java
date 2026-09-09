package de.elhansalaji.ticket_system.ticket;

import jakarta.validation.constraints.NotBlank;

public record TicketRequest(

        @NotBlank(message = "Titel darf nicht leer sein")
        String title,
        String description
) {
}