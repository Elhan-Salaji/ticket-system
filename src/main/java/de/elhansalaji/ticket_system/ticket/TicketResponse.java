package de.elhansalaji.ticket_system.ticket;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketStatus status
) {

    public static TicketResponse fromEntity(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus()
        );
    }
}