/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.validation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tom
 * 
 */
public class ViolationListener implements PropertyChangeListener {

    private List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

    /**
     * clear all events
     *
     */
    public void clearEvents() {
        events.clear();
    }

    /**
     * @return the events
     */
    public List<PropertyChangeEvent> getEvents() {
        return events;
    }

    /**
     * get events for a property
     * 
     * @param property
     * @return
     */
    public List<PropertyChangeEvent> getEvents(String property) {
        List<PropertyChangeEvent> matchingEvents = new ArrayList<PropertyChangeEvent>();
        for (PropertyChangeEvent evt : events) {
            if (evt.getPropertyName().equals(property)) {
                matchingEvents.add(evt);
            }
        }
        return matchingEvents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans. PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        events.add(arg0);
    }

}
