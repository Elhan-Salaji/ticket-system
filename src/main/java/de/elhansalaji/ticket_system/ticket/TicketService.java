package de.elhansalaji.ticket_system.ticket;

import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    // Konstruktor
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Methode zum Erstellen eines Tickets
    public Ticket createTicket(String title, String description) {
        Ticket ticket = new Ticket(title, description);
        return ticketRepository.save(ticket);
    }

    // Methode zum Abrufen eines Tickets
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}