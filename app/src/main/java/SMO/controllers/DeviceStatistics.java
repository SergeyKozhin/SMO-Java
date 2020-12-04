package SMO.controllers;

public class DeviceStatistics {
    private final int number;
    private final double k;

    public DeviceStatistics(int number, double k) {
        this.number = number;
        this.k = k;
    }

    public int getNumber() {
        return number;
    }

    public double getK() {
        return k;
    }
}
