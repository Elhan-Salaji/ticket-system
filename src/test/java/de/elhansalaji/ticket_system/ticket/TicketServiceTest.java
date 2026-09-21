package de.elhansalaji.ticket_system.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Unit-Test ohne Spring-Kontext: das Repository wird gemockt, getestet wird nur die Logik im Service.
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("createTicket speichert ein neues Ticket mit Titel, Beschreibung und Status OPEN")
    void createTicketSavesNewOpenTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ticketService.createTicket("Drucker defekt", "Drucker im 2. OG druckt nicht");

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        Ticket saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Drucker defekt");
        assertThat(saved.getDescription()).isEqualTo("Drucker im 2. OG druckt nicht");
        assertThat(saved.getStatus()).isEqualTo(TicketStatus.OPEN);
        assertThat(saved.getId()).isNull();
    }

    @Test
    @DisplayName("createTicket gibt das vom Repository zurückgegebene Ticket weiter")
    void createTicketReturnsSavedTicket() {
        Ticket persisted = new Ticket("Drucker defekt", null);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(persisted);

        Ticket result = ticketService.createTicket("Drucker defekt", null);

        assertThat(result).isSameAs(persisted);
    }

    @Test
    @DisplayName("createTicket akzeptiert ein Ticket ohne Beschreibung")
    void createTicketWithoutDescription() {
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.createTicket("Drucker defekt", null);

        assertThat(result.getDescription()).isNull();
    }

    @Test
    @DisplayName("getTicketById gibt das gefundene Ticket zurück")
    void getTicketByIdReturnsTicket() {
        Ticket ticket = new Ticket("Drucker defekt", "Drucker im 2. OG druckt nicht");
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicketById(1L);

        assertThat(result).isSameAs(ticket);
    }

    @Test
    @DisplayName("getTicketById wirft TicketNotFoundException, wenn es die ID nicht gibt")
    void getTicketByIdThrowsWhenMissing() {
        when(ticketRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(42L))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Ticket mit ID 42 wurde nicht gefunden");
    }

    @Test
    @DisplayName("getAllTickets fragt das Repository absteigend nach ID sortiert ab")
    void getAllTicketsSortsByIdDescending() {
        when(ticketRepository.findAll(any(Sort.class))).thenReturn(List.of());

        ticketService.getAllTickets();

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        verify(ticketRepository).findAll(captor.capture());
        assertThat(captor.getValue()).isEqualTo(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    @DisplayName("getAllTickets gibt die Tickets des Repositories zurück")
    void getAllTicketsReturnsRepositoryResult() {
        List<Ticket> tickets = List.of(
                new Ticket("Drucker defekt", null),
                new Ticket("Monitor flackert", null));
        when(ticketRepository.findAll(any(Sort.class))).thenReturn(tickets);

        List<Ticket> result = ticketService.getAllTickets();

        assertThat(result).isSameAs(tickets);
    }

    @Test
    @DisplayName("getAllTickets gibt eine leere Liste zurück, wenn es keine Tickets gibt")
    void getAllTicketsReturnsEmptyListWhenNoTickets() {
        when(ticketRepository.findAll(any(Sort.class))).thenReturn(List.of());

        assertThat(ticketService.getAllTickets()).isEmpty();
    }
}
