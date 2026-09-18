package com.project.ticketing.service;

import com.project.ticketing_concurrency_lab.TicketingApplication;
import com.project.ticketing_concurrency_lab.domain.Ticket;
import com.project.ticketing_concurrency_lab.domain.User;
import com.project.ticketing_concurrency_lab.repository.IssuedTicketRepository;
import com.project.ticketing_concurrency_lab.repository.TicketRepository;
import com.project.ticketing_concurrency_lab.repository.UserRepository;
import com.project.ticketing_concurrency_lab.service.TicketService;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TicketingApplication.class)
class TicketServiceConcurrencyTest {

    private static final int TICKET_STOCK = 100;
    private static final int REQUEST_COUNT = 1_000;

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final IssuedTicketRepository issuedTicketRepository;

    @Autowired
    TicketServiceConcurrencyTest(
            TicketService ticketService,
            UserRepository userRepository,
            TicketRepository ticketRepository,
            IssuedTicketRepository issuedTicketRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.issuedTicketRepository = issuedTicketRepository;
    }

    @AfterEach
    void setup() {
        issuedTicketRepository.deleteAll();
        userRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    void 동시성_테스트() throws Exception {
        Long ticketId = ticketService.createTicket("concert", TICKET_STOCK);
        List<Long> userIds = userRepository.saveAll(
                        java.util.stream.IntStream.range(0, REQUEST_COUNT)
                                .mapToObj(ignored -> User.create())
                                .toList())
                .stream()
                .map(User::getId)
                .toList();

        CountDownLatch ready = new CountDownLatch(REQUEST_COUNT);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var requests = userIds.stream()
                    .map(userId -> executor.submit(() -> {
                        ready.countDown();
                        start.await();
                        ticketService.issue(ticketId, userId);
                        return null;
                    }))
                    .toList();

            ready.await();
            start.countDown();

            int successCount = 0;
            int failureCount =0;
            Throwable firstFailure = null;
            for (Future<?> request : requests) {

                try {

                    request.get();
                    successCount++;
                } catch (ExecutionException e) {
                    failureCount++;
                    if(firstFailure ==null) {
                        firstFailure= e.getCause();
                    }
                }
            }
            System.out.printf("성공=%d, 실패=%d\n", successCount,failureCount);
            if (firstFailure !=null) {
                firstFailure.printStackTrace();
            }
        }

        Ticket ticket = ticketService.findTicket(ticketId);
        long issuedCount = issuedTicketRepository.count();

        System.out.println("issuedCount = " + issuedCount);
        System.out.println("ticket.count ="+ ticket.getQuantityTicket());
        assertThat(issuedCount).isGreaterThan(TICKET_STOCK);
        assertThat(ticket.getQuantityTicket()).isGreaterThanOrEqualTo(0);
    }
}
