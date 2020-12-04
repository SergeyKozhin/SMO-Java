package SMO.model.elements;

import java.util.Random;

public class Device {
    private final int number;
    private final double alpha;
    private final double beta;

    private final Random random = new Random();

    private Request currentRequest = null;

    public Device(int number, double alpha, double beta) {
        this.number = number;
        this.alpha = alpha;
        this.beta = beta;
    }

    public void put(double currentTime, Request request) {
        request.setDeviceNumber(number);
        request.setDeviceTime(currentTime);
        request.setReleaseTime(currentTime + alpha + (beta - alpha) * random.nextDouble());

        currentRequest = request;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
    }
}
