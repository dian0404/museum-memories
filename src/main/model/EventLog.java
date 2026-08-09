package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Code copied from the EventLog class in the CPSC 210 Alarm System application.

// Represents a log of events using the Singleton Design Pattern
public class EventLog implements Iterable<Event> {
    /** the only EventLog in the system (Singleton Design Pattern) */
    private static EventLog theLog;
    private Collection<Event> events;

    // EFFECTS: constructs an empty event log
    private EventLog() {
        events = new ArrayList<Event>();
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     * 
     * @return instance of EventLog
     */

    // EFFECTS: returns the single instance of EventLog;
    // creates it first if it does not already exist
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    /**
     * Adds an event to the event log.
     * 
     * @param e the event to be added
     */

    // MODIFIES: this
    // EFFECTS: adds e to this event log
    public void logEvent(Event e) {
        events.add(e);
    }

    /**
     * Clears the event log and logs the event.
     */

    // MODIFIES: this
    // EFFECTS: clears this event log and logs an event indicating
    // that the event log was cleared
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: returns an iterator over the events in this event log
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}