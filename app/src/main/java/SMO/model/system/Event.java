package SMO.model.system;

import SMO.model.elements.Request;

public class Event {
    private final EventType type;
    private final double time;
    private final Request request;

    public Event(EventType type, double time, Request request) {
        this.type = type;
        this.time = time;
        this.request = request;
    }

    public EventType getType() {
        return type;
    }

    public Request getRequest() {
        return request;
    }

    public double getTime() {
        return time;
    }
}
