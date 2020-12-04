package SMO.model.system;

import SMO.model.elements.Buffer;
import SMO.model.elements.Device;
import SMO.model.elements.Request;
import SMO.model.elements.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimulationSystem {
    private final List<Source> sources;
    private final Buffer buffer;
    private final List<Device> devices;

    private final List<Request> processed = new ArrayList<>();
    private final List<Request> rejected = new ArrayList<>();

    private final List<Event> events = new ArrayList<>();

    private final int requestsNum;

    private int generatedCount = 0;
    private int requestCount = 0;

    private Event lastEvent = new Event(EventType.SIMULATION_STARTED, 0, null);

    public SimulationSystem(int bufferSize, List<SourceConfig> sourceConfigs, List<DeviceConfig> deviceConfigs, int requestsNum) {
        sources = new ArrayList<>(sourceConfigs.size());
        for (int i = 0; i < sourceConfigs.size(); i++) {
            sources.add(new Source(i, sourceConfigs.get(i).getFrequency()));
        }

        buffer = new Buffer(bufferSize);

        devices = new ArrayList<>(deviceConfigs.size());
        for (int i = 0; i < deviceConfigs.size(); i++) {
            devices.add(new Device(i, deviceConfigs.get(i).getAlpha(), deviceConfigs.get(i).getBeta()));
        }

        this.requestsNum = requestsNum;

        for (Source source : sources) {
            if (generatedCount == requestsNum) {
                break;
            }
            Request request = source.nextRequest(0);
            generatedCount++;
            addEvent(new Event(EventType.REQUEST_CREATED, request.getCreationTime(), request));
        }
    }

    public Event makeNextStep() {
        if (events.isEmpty()) {
            return new Event(EventType.SIMULATION_FINISHED, lastEvent.getTime(), null);
        }

        lastEvent = events.remove(events.size() - 1);
        Request request = lastEvent.getRequest();
        request.setNumber(requestCount);
        requestCount++;

        switch (lastEvent.getType()) {
            case REQUEST_CREATED -> {
                Optional<Request> requestOptional = placeRequest(lastEvent.getTime(), request);
                requestOptional.ifPresent(r -> {
                    r.setReleaseTime(lastEvent.getTime());
                    rejected.add(r);
                });

                if (generatedCount != requestsNum) {
                    Request nextRequest = sources.get(request.getSourceNumber()).nextRequest(lastEvent.getTime());
                    generatedCount++;
                    addEvent(new Event(EventType.REQUEST_CREATED, nextRequest.getCreationTime(), nextRequest));
                }
            }
            case DEVICE_FINISHED -> {
                processed.add(request);
                devices.get(request.getDeviceNumber()).setCurrentRequest(null);
                processNextRequest(lastEvent.getTime());
            }
        }

        return lastEvent;
    }

    private void addEvent(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (event.getTime() > events.get(i).getTime()) {
                events.add(i, event);
                return;
            }
        }
        events.add(event);
    }

    private Optional<Request> placeRequest(double currentTime, Request request) {
        for (Device device : devices) {
            if (device.getCurrentRequest() == null) {
                device.put(currentTime, request);
                addEvent(new Event(EventType.DEVICE_FINISHED, request.getReleaseTime(), request));
                return Optional.empty();
            }
        }

        return buffer.put(request);
    }

    private void processNextRequest(double currentTime) {
        Optional<Request> requestOptional = buffer.pull();
        requestOptional.ifPresent(request -> {
            for (Device device : devices) {
                if (device.getCurrentRequest() == null) {
                    device.put(currentTime, request);
                    addEvent(new Event(EventType.DEVICE_FINISHED, request.getReleaseTime(), request));
                    return;
                }
            }
        });
    }

    public int getRequestCount() {
        return requestCount;
    }

    public int getSourcesCount() {
        return sources.size();
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<Request> getProcessed() {
        return processed;
    }

    public List<Request> getRejected() {
        return rejected;
    }
}
