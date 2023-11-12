package org.retailer.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retailer.demo.domain.dto.PointsByMonth;
import org.retailer.demo.exception.AwardingPointsException;
import org.retailer.demo.service.SpendingsService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwardingPointsControllerTest {

    @Mock
    SpendingsService spendingsService;
    @InjectMocks
    AwardingPointsController awardingPointsController;

    LocalDate date;
    LocalDate dateException;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        dateException = LocalDate.now().plusDays(1);
    }

    @Test
    void getAwardingPointsTest() {
        List<PointsByMonth> pointsByMonths = awardingPointsController.getAwardingPoints(date).getBody();
        assertNotNull(pointsByMonths);
        assertTrue(pointsByMonths instanceof List);
    }

    @Test
    void getAwardingPointsExceptionDateTest() {
        Throwable tr = assertThrows(Exception.class, () -> awardingPointsController.getAwardingPoints(dateException));
        assertEquals("fromDate cannot be in the future", tr.getLocalizedMessage());
    }

    @Test
    void exceptionHandlerDateTest() {
        String message = awardingPointsController.exceptionHandler(new AwardingPointsException("Exception")).getBody();
        assertNotNull(message);
    }
}