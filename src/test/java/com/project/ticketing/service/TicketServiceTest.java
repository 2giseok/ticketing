package com.project.ticketing.service;

import com.project.ticketing_concurrency_lab.domain.IssuedTicket;
import com.project.ticketing_concurrency_lab.domain.Ticket;
import com.project.ticketing_concurrency_lab.domain.User;
import com.project.ticketing_concurrency_lab.repository.IssuedTicketRepository;
import com.project.ticketing_concurrency_lab.repository.TicketRepository;
import com.project.ticketing_concurrency_lab.repository.UserRepository;
import com.project.ticketing_concurrency_lab.service.TicketService;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private UserRepository userRepository;
    @Mock private IssuedTicketRepository issuedTicketRepository;
    @InjectMocks private TicketService ticketService;

    @Test
    void issue_decreasesTicketStockAndSavesIssuedTicket() {
        Ticket ticket = Ticket.create("concert", 2);
        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        given(userRepository.findById(10L)).willReturn(Optional.of(User.create()));

        ticketService.issue(1L, 10L);

        assertThat(ticket.getQuantityTicket()).isEqualTo(1);
        then(issuedTicketRepository).should().save(any(IssuedTicket.class));
    }

    @Test
    void issue_whenStockIsInsufficient_throwsAndDoesNotSaveIssuedTicket() {
        Ticket ticket = Ticket.create("concert", 0);
        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        given(userRepository.findById(10L)).willReturn(Optional.of(User.create()));

        assertThatThrownBy(() -> ticketService.issue(1L, 10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("재고가 부족합니다");

        then(issuedTicketRepository).shouldHaveNoInteractions();
    }

    @Test
    void findTicket_returnsTicketFoundById() {
        Ticket ticket = Ticket.create("concert", 100);
        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));

        assertThat(ticketService.findTicket(1L)).isSameAs(ticket);
    }

    @Test
    void findTicket_whenTicketDoesNotExist_throwsException() {
        given(ticketRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findTicket(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createTicket_initializesStockAndReturnsSavedId() {
        Ticket persistedTicket = mock(Ticket.class);
        given(persistedTicket.getId()).willReturn(1L);
        given(ticketRepository.save(any(Ticket.class))).willReturn(persistedTicket);

        Long ticketId = ticketService.createTicket("concert", 100);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        then(ticketRepository).should().save(ticketCaptor.capture());
        Ticket savedTicket = ticketCaptor.getValue();
        assertThat(ticketId).isEqualTo(1L);
        assertThat(savedTicket.getName()).isEqualTo("concert");
        assertThat(savedTicket.getTotalTicket()).isEqualTo(100);
        assertThat(savedTicket.getQuantityTicket()).isEqualTo(100);
    }
}
