package SMO.controllers;

import SMO.model.system.Event;

public class SystemState {
    private final Event event;
    private final int created;
    private final int processed;
    private final int rejected;

    public SystemState(Event event, int created, int processed, int rejected) {
        this.event = event;
        this.created = created;
        this.processed = processed;
        this.rejected = rejected;
    }

    public Event getEvent() {
        return event;
    }

    public int getCreated() {
        return created;
    }

    public int getProcessed() {
        return processed;
    }

    public int getRejected() {
        return rejected;
    }
}
