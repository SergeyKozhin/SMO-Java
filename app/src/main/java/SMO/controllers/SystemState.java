package SMO.controllers;

import SMO.model.system.Event;

public class SystemState {
    private final Event event;
    private final int requestNumber;
    private final int processed;
    private final int rejected;

    public SystemState(Event event, int requestNumber, int processed, int rejected) {
        this.event = event;
        this.requestNumber = requestNumber;
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

    public int getRequestNumber() {
        return requestNumber;
    }
}
