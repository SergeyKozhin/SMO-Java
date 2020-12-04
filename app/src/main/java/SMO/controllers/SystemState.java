package SMO.controllers;

import SMO.model.system.Event;

public class SystemState {
    private final Event event;
    private final int processed;
    private final int rejected;

    public SystemState(Event event, int processed, int rejected) {
        this.event = event;
        this.processed = processed;
        this.rejected = rejected;
    }

    public Event getEvent() {
        return event;
    }

    public int getProcessed() {
        return processed;
    }

    public int getRejected() {
        return rejected;
    }
}
