package de.elhansalaji.ticket_system.ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


// Das hier ist das Ticket Objekt selbst, es hat eine Id, einen Titel und eine Beschreibung, die Getter und Setter werden über Lombok generiert beim Kompilieren.
@Getter
@Entity
public class Ticket {

    // Die ID ist ein Primärschlüssel und wird automatisch generiert per auto increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Der Titel ist ein String und kann nicht null sein
    @Setter
    @Column(nullable = false)
    private String title;

    // Die Beschreibung ist ein String und kann null sein, die Länge ist auf 2000 Zeichen begrenzt
    @Setter
    @Column(length = 2000)
    private String description;

    // Der Status ist ein Enum und kann nicht null sein, der Standardwert ist "OPEN"
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    // Dieser Konstruktor ist nur für JPA wichtig.
    protected Ticket() {
    }

    //Beim Erstellen, eines Tickets werden ein Titel und eine Beschreibung angegeben, ID und Status werden automatisch generiert/vergeben.
    public Ticket(String title, String description) {
        this.title = title;
        this.description = description;
    }

}