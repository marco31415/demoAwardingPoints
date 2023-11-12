package org.retailer.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retailer.demo.domain.dto.PointsByMonth;
import org.retailer.demo.domain.entity.Customer;
import org.retailer.demo.domain.entity.Spendings;
import org.retailer.demo.repository.CustomerRepository;
import org.retailer.demo.repository.SpendingsRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpendingsServiceTest {

    SpendingsRepository spendingsRepository;
    CustomerRepository customerRepository;

    SpendingsService spendingsService;

    LocalDate date;
    List<Customer> customers;
    List<Spendings> spendings;

    @BeforeEach
    void setUp() {
        customers = List.of(new Customer(1, "John Doe", "test@mail.com"));
        spendings = List.of(new Spendings(1, customers.get(0), "", 60, LocalDate.now()),
                new Spendings(1, customers.get(0), "", 100, LocalDate.now()));

        date = LocalDate.now();
        spendingsRepository = Mockito.mock(SpendingsRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        spendingsService = new SpendingsService(spendingsRepository, customerRepository, 1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAwardingPointsTest() {
        when(customerRepository.findAll()).thenReturn(customers);
        when(spendingsRepository.findAllByCustomerIdAndDateBetween(anyLong(), any(), any())).thenReturn(spendings);
        List<PointsByMonth> points = spendingsService.getAwardingPoints(date);

        assertEquals(60, points.get(0).getAwardedPoints().get(0).getPoints());
    }

    @Test
    void getAwardingPointsNoCustomersTest() {
        when(customerRepository.findAll()).thenReturn(null);
        Throwable tr = assertThrows(Exception.class, () -> spendingsService.getAwardingPoints(date));
        assertEquals("No customers found", tr.getLocalizedMessage());
    }

    @Test
    void getAwardingPointsMonthsExceptionTest() {
        spendingsService = new SpendingsService(spendingsRepository, customerRepository, 0);
        Throwable tr = assertThrows(Exception.class, () -> spendingsService.getAwardingPoints(date));
        assertEquals("monthsToSearch env value must be greater than 0", tr.getLocalizedMessage());
    }
}