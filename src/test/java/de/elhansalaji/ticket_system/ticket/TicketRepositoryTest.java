package de.elhansalaji.ticket_system.ticket;

import de.elhansalaji.ticket_system.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Integrationstest gegen eine echte PostgreSQL im Testcontainer. Jeder Test läuft in einer Transaktion,
// die am Ende zurückgerollt wird, die Tests beeinflussen sich also nicht gegenseitig.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("save vergibt eine ID und das Ticket lässt sich wieder aus der Datenbank laden")
    void saveAndFindById() {
        Ticket saved = ticketRepository.save(new Ticket("Drucker defekt", "Drucker im 2. OG druckt nicht"));
        // Persistence Context leeren, damit findById wirklich aus der Datenbank liest
        entityManager.flush();
        entityManager.clear();

        Ticket found = ticketRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Drucker defekt");
        assertThat(found.getDescription()).isEqualTo("Drucker im 2. OG druckt nicht");
        assertThat(found.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    @DisplayName("findById liefert ein leeres Optional für eine unbekannte ID")
    void findByIdUnknown() {
        assertThat(ticketRepository.findById(999_999L)).isEmpty();
    }

    @Test
    @DisplayName("Der Status wird als Text und nicht als Ordinalzahl gespeichert")
    void statusIsStoredAsString() {
        Ticket saved = ticketRepository.saveAndFlush(new Ticket("Drucker defekt", null));

        Object status = entityManager.getEntityManager()
                .createNativeQuery("select status from ticket where id = :id")
                .setParameter("id", saved.getId())
                .getSingleResult();

        assertThat(status).isEqualTo("OPEN");
    }

    @Test
    @DisplayName("Ein Ticket ohne Titel wird von der Datenbank abgelehnt")
    void titleMustNotBeNull() {
        assertThatThrownBy(() -> ticketRepository.saveAndFlush(new Ticket(null, "ohne Titel")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Eine Beschreibung mit genau 2000 Zeichen wird gespeichert")
    void descriptionWithMaxLength() {
        String description = "a".repeat(2000);

        Ticket saved = ticketRepository.saveAndFlush(new Ticket("Lange Beschreibung", description));
        entityManager.clear();

        assertThat(ticketRepository.findById(saved.getId()).orElseThrow().getDescription()).hasSize(2000);
    }

    @Test
    @DisplayName("Eine Beschreibung mit mehr als 2000 Zeichen wird von der Datenbank abgelehnt")
    void descriptionLongerThanMaxLength() {
        String description = "a".repeat(2001);

        assertThatThrownBy(() -> ticketRepository.saveAndFlush(new Ticket("Zu lange Beschreibung", description)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
