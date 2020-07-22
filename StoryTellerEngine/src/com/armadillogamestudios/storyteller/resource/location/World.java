package com.armadillogamestudios.storyteller.resource.location;

import com.armadillogamestudios.storyteller.scenario.event.Event;
import com.armadillogamestudios.storyteller.scenario.event.EventComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class World extends Location<World> {
    private PriorityQueue<Event<?>> events = new PriorityQueue<>(new EventComparator());
    private List<Region> regions = new ArrayList<>();

    private DateTime dateTime = new DateTime();

    public World(String name) {
        super(name, null);
    }

    public Event<?> passTime(int minutes) {
        Event<?> next = events.peek();

        if (next != null && next.getTriggerTime() <= dateTime.getTime() + minutes) {
            events.remove();
            dateTime.passTimeTo(next.getTriggerTime());

            return next;
        } else {
            dateTime.passTime(minutes);

            return null;
        }
    }

    public void addRegion(Region region) {
        regions.add(region);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void addEvent(Event<?> event) {
        events.add(event);
    }
}