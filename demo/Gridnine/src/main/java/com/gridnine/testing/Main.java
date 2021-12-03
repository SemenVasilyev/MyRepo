package com.gridnine.testing;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Semen V
 * @created 01|12|2021
 */
public class Main {
    static List<Flight> flightsDepartureBeforeNow = new ArrayList<>();
    static List<Flight> flightsIsBadSegment = new ArrayList<>();
    static List<Flight> flightsLongWaitingTime = new ArrayList<>();
    static List<Flight> list = new LinkedList<>(FlightBuilder.createFlights());

    public static void main(String[] args) {

        filter(list);

        System.out.println("Обычный полет: " + list);
        System.out.println("Полет с битыми сегментами: " + flightsIsBadSegment);
        System.out.println("Полет с датой вылета раньше текущей: " + flightsDepartureBeforeNow);
        System.out.println("Полет со временем ожидания более 2-х часов: " + flightsLongWaitingTime);

    }

    private static void filter(List<Flight> list) {
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            boolean deleteInList = false;
            Flight flight = list.get(i);
            /*
            в задаче не указанно может ли полет попадать в несколько списков, по этому
            реализовал таким образом, что битые сегмент попадает только в один список(оно и логично),
            полет с датой вылета раньше текущей и полет с долгим ожиданием может попасть в два списка
            */
            if (isBadSegments(flight)) {
                flightsIsBadSegment.add(flight);
                deleteInList = true;
            } else {
                if (isDepartureBeforeNow(flight)) {
                    flightsDepartureBeforeNow.add(flight);
                    deleteInList = true;
                }
                Duration totalWaitingTime = totalWaitingTime(flight);
                long hours = totalWaitingTime.toMinutes();
                if (hours > 120) {
                    flightsLongWaitingTime.add(flight);
                    deleteInList = true;
                }
            }
            if (deleteInList) {
                list.remove(i);
                i--;
                listSize--;
            }
        }
    }

    public static boolean isBadSegments(Flight flight) {
        Optional<Integer> optional = flight.getSegments().stream()
                .map(s -> s.getArrivalDate().compareTo(s.getDepartureDate()))
                .filter(n -> (n < 0)).findFirst();

        return optional.isEmpty() ? false : true;
    }

    public static boolean isDepartureBeforeNow(Flight flight) {
        Optional<Integer> optional = flight.getSegments().stream()
                .map(s -> s.getDepartureDate().compareTo(LocalDateTime.now()))
                .filter(n -> (n < 0)).findFirst();

        return optional.isEmpty() ? false : true;
    }

    public static Duration totalWaitingTime(Flight flight) {
        List<Segment> segments = flight.getSegments();
        Duration duration = Duration.ZERO;
        for (int i = 0; i < segments.size() - 1; i++) {
            duration = duration.plus(waitingTime(segments.get(i), segments.get(i + 1)));
            System.out.println("");
        }
        return duration;
    }

    public static Duration waitingTime(Segment segment1, Segment segment2) {
        LocalDateTime arrivalDate = segment1.getArrivalDate();
        LocalDateTime departureDate = segment2.getDepartureDate();
        return Duration.between(arrivalDate, departureDate);
    }
}

