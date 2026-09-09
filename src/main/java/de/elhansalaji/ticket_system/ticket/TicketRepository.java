package de.elhansalaji.ticket_system.ticket;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Ticket, Long> sagt, dass dieses Repository Objekte vom Typ Ticket verwaltet und der Primär schlüssel vom Typ Long ist.
// Spring erzeugt zur Laufzeit automatisch eine Klasse, die dieses Interface implementiert, mit fertigen Methoden wie save(ticket), findById(id), findAll(), deleteById(id)
// Der Code redet nur mit diesem Interface hier nie direkt mit JDBC oder SQL
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}