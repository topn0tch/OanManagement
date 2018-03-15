package com.oan.management.service.calendar;

import com.oan.management.model.Event;
import com.oan.management.model.User;

import java.util.List;

/**
 * @author Oan Stultjens
 * @since 26/01/2018.
 */
public interface EventService {
    List<Event> findAllByUser(User user);
    Event save(Event event);
    void delete(Event event);
    Event findById(Long id);
    Event editById(Long id, String title, String start, String end);
    Event editEventAndColour(Event event, String title, String description, String backgroundColour, String borderColour);
}
