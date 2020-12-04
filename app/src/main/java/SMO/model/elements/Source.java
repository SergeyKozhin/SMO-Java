package SMO.model.elements;

import java.util.Random;

public class Source {
    private final int number;
    private final double frequency;
    private final Random random = new Random();

    public Source(int number, double frequency) {
        this.number = number;
        this.frequency = frequency;
    }

    public Request nextRequest(double currentTime) {
        return new Request(number, currentTime - 1 / frequency * Math.log(random.nextDouble()));
    }

}
