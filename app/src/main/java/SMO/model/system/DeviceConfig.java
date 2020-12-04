package SMO.model.system;

public class DeviceConfig {
    private final double alpha;
    private final double beta;


    public DeviceConfig(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }
}
