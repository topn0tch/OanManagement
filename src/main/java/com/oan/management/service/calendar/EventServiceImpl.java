package com.oan.management.service.calendar;

import com.oan.management.model.Event;
import com.oan.management.model.User;
import com.oan.management.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link EventService}
 * @author Oan Stultjens
 * @since 26/01/2018.
 */

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    EventRepository eventRepository;

    @Override
    public List<Event> findAllByUser(User user) {
        return eventRepository.findAllByUser(user);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Edits an event by the specified id, only basic information of the event is passed
     * This one is used for drag and drop in the calendar
     * @param id Long
     * @param title String
     * @param start String
     * @param end String
     * @return Event
     */
    @Override
    public Event editById(Long id, String title, String start, String end) {
        Event event = eventRepository.findById(id);
        event.setStart(start);
        event.setEnd(end);
        event.setTitle(title);
        event.setDescription(event.getDescription());
        return eventRepository.save(event);
    }

    /**
     * Edits an event with all available information, colour is included aswell
     * This one is used for the 'edit event' modal
     * @param event Event
     * @param title String
     * @param description String
     * @param backgroundColour String (HEX colour)
     * @param borderColour String (HEX colour)
     * @return Event
     */
    @Override
    public Event editEventAndColour(Event event, String title, String description, String backgroundColour, String borderColour) {
        event.setTitle(title);
        event.setDescription(description);
        event.setBackgroundColor(backgroundColour);
        event.setBorderColor(borderColour);
        return eventRepository.save(event);
    }
}
