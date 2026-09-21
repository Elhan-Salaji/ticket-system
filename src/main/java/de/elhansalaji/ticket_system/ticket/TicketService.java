package de.elhansalaji.ticket_system.ticket;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    // Methode zum Abrufen aller Tickets, neueste zuerst (höchste ID zuerst)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}