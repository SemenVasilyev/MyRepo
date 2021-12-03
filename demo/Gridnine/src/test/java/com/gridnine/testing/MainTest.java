package com.gridnine.testing;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Semen V
 * @created 03|12|2021
 */
class MainTest {
   static LocalDateTime dateNow = LocalDateTime.now();

    @Test
    void testIsBadSegments() throws Exception {
        Flight badFlight = new Flight(Arrays.asList(new Segment(dateNow,dateNow.plusHours(1)),new Segment(dateNow.plusHours(6),dateNow.plusHours(5))));
        Flight goodFlight = new Flight(Arrays.asList(new Segment(dateNow,dateNow.plusHours(2)),new Segment(dateNow.plusHours(3),dateNow.plusHours(4))));

        boolean badSegments = Main.isBadSegments(badFlight);
        boolean goodSegments = Main.isBadSegments(goodFlight);

        Assert.assertEquals(true,badSegments);
        Assert.assertEquals(false, goodSegments);
    }

    @Test
    void testIsDepartureBeforeNow() {
        Flight badFlight = new Flight(Arrays.asList(new Segment(dateNow.minusHours(1),dateNow.plusHours(1)),new Segment(dateNow.plusHours(6),dateNow.plusHours(5))));
        Flight goodFlight = new Flight(Arrays.asList(new Segment(dateNow,dateNow.plusHours(2)),new Segment(dateNow.plusHours(3),dateNow.plusHours(4))));

        boolean departureBeforeNow = Main.isBadSegments(badFlight);
        boolean notDepartureBeforeNow = Main.isBadSegments(goodFlight);

        Assert.assertEquals(true,departureBeforeNow);
        Assert.assertEquals(false, notDepartureBeforeNow);
    }

    @Test
    void testTotalWaitingTime() {
        Flight longWaitFlight = new Flight(Arrays.asList(new Segment(dateNow,dateNow.plusHours(1)),new Segment(dateNow.plusHours(5),dateNow.plusHours(6))));
        Flight normalWaitFlight = new Flight(Arrays.asList(new Segment(dateNow,dateNow.plusHours(2)),new Segment(dateNow.plusHours(3),dateNow.plusHours(4))));

        Duration longWait = Main.totalWaitingTime(longWaitFlight);
        Duration normalWait = Main.totalWaitingTime(normalWaitFlight);

        Assert.assertEquals(true,longWait.toMinutes() > 120);
        Assert.assertEquals(false, normalWait.toMinutes() > 120);
    }

}