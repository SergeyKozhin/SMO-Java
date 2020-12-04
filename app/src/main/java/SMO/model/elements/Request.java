package SMO.model.elements;

public class Request {
    private final int sourceNumber;
    private final double creationTime;

    private int number = 0;
    private int deviceNumber = 0;
    private double deviceTime = 0;
    private double releaseTime = 0;
    private boolean rejected = false;

    public Request(int sourceNumber, double creationTime) {
        this.sourceNumber = sourceNumber;
        this.creationTime = creationTime;
    }


    public double getCreationTime() {
        return creationTime;
    }

    public double getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(double deviceTime) {
        this.deviceTime = deviceTime;
    }

    public double getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(double releaseTime) {
        this.releaseTime = releaseTime;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
