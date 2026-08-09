package model;

import java.util.Calendar;
import java.util.Date;

// Code copied from the Event class in the CPSC 210 Alarm System application.

// Represents an event with a description and the date and time it was logged
public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    /**
     * Creates an event with the given description
     * and the current date/time stamp.
     * 
     * @param description a description of the event
     */

    // EFFECTS: constructs an event with the given description and
    // the current date and time
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    /**
     * Gets the date of this event (includes time).
     * 
     * @return the date of the event
     */
    public Date getDate() {
        return dateLogged;
    }

    /**
     * Gets the description of this event.
     * 
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns true if other is an Event with the same date,
    // time and description as this event; otherwise returns false
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description));
    }

    // EFFECTS: returns the hash code for this event
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    // EFFECTS: returns a string representation of this event containing
    // its date, time and description
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}