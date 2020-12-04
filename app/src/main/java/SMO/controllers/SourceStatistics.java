package SMO.controllers;

public class SourceStatistics {
    private final int number;
    private final int created;
    private final double pReject;
    private final double tSystem;
    private final double tWait;
    private final double dWait;
    private final double tProcess;
    private final double dProcess;

    public SourceStatistics(int number, int created, double pReject, double tSystem,
                            double tWait, double dWait, double tProcess, double dProcess) {
        this.number = number;
        this.created = created;
        this.pReject = pReject;
        this.tSystem = tSystem;
        this.tWait = tWait;
        this.dWait = dWait;
        this.tProcess = tProcess;
        this.dProcess = dProcess;
    }

    public int getNumber() {
        return number;
    }

    public int getCreated() {
        return created;
    }

    public double getPReject() {
        return pReject;
    }

    public double getTSystem() {
        return tSystem;
    }

    public double getTWait() {
        return tWait;
    }

    public double getDWait() {
        return dWait;
    }

    public double getTProcess() {
        return tProcess;
    }

    public double getDProcess() {
        return dProcess;
    }
}
