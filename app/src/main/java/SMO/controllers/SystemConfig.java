package SMO.controllers;

import SMO.model.system.DeviceConfig;
import SMO.model.system.SourceConfig;

import java.util.List;

public class SystemConfig {
    private int bufferSize;
    private int requestNumber;
    private List<SourceConfig> sourceConfigs;
    private List<DeviceConfig> deviceConfigs;

    public SystemConfig(int bufferSize, int requestNumber, List<SourceConfig> sourceConfigs, List<DeviceConfig> deviceConfigs) {
        this.bufferSize = bufferSize;
        this.requestNumber = requestNumber;
        this.sourceConfigs = sourceConfigs;
        this.deviceConfigs = deviceConfigs;
    }

    public SystemConfig() {}

    public int getBufferSize() {
        return bufferSize;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public List<SourceConfig> getSourceConfigs() {
        return sourceConfigs;
    }

    public List<DeviceConfig> getDeviceConfigs() {
        return deviceConfigs;
    }
}
