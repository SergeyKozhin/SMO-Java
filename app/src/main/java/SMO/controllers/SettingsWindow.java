package SMO.controllers;

import SMO.App;
import SMO.model.system.DeviceConfig;
import SMO.model.system.Event;
import SMO.model.system.EventType;
import SMO.model.system.SimulationSystem;
import SMO.model.system.SourceConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsWindow {
    @FXML
    private VBox sourceConfigs;
    @FXML
    private VBox deviceConfigs;
    @FXML
    private ScrollPane sourcesScroll;
    @FXML
    private ScrollPane devicesScroll;
    @FXML
    private TextField bufferSIzeText;
    @FXML
    private TextField requestNumberText;

    private App app;
    private final FileChooser fileChooser = new FileChooser();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @FXML
    private void initialize() {

    }

    @FXML
    private SourceConfigComponent addSourceConfig() {
        SourceConfigComponent component = new SourceConfigComponent();
        component.setContainer(sourceConfigs);
        sourceConfigs.getChildren().add(component);
        component.getChildren().get(1).requestFocus();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sourcesScroll.setVvalue(1.0);
            }
        }, 50);

        return component;
    }

    @FXML
    private DeviceConfigComponent addDeviceConfig() {
        DeviceConfigComponent component = new DeviceConfigComponent();
        component.setContainer(deviceConfigs);
        deviceConfigs.getChildren().add(component);
        component.getChildren().get(1).requestFocus();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                devicesScroll.setVvalue(1.0);
            }
        }, 50);

        return component;
    }

    @FXML
    private void loadFromFile() {
        File file = fileChooser.showOpenDialog(app.getPrimaryStage());
        if (file == null) {
            return;
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            applyConfig(gson.fromJson(reader, SystemConfig.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Invalid file");
            alert.setHeaderText("Please choose valid configuration file");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    @FXML
    private void saveToFile() {
        SystemConfig config = createConfig();
        if (config == null) {
            return;
        }

        File file = fileChooser.showSaveDialog(app.getPrimaryStage());
        if (file == null) {
            return;
        }
        try (Writer writer = new FileWriter(file)){
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openStepMode() {
        SystemConfig config = createConfig();
        if (config == null) {
            return;
        }

        app.setConfig(config);
        SimulationSystem system = new SimulationSystem(
                config.getBufferSize(),
                config.getSourceConfigs(),
                config.getDeviceConfigs(),
                config.getRequestNumber()
        );

       app.openStepModeWindow(system);
    }

    private void applyConfig(SystemConfig config) {
        if (config == null) {
            return;
        }
        sourceConfigs.getChildren().clear();
        for (SourceConfig sourceConfig: config.getSourceConfigs()) {
            SourceConfigComponent component = addSourceConfig();
            component.setLambda(sourceConfig.getFrequency());
        }

        deviceConfigs.getChildren().clear();
        for (DeviceConfig deviceConfig: config.getDeviceConfigs()) {
            DeviceConfigComponent component = addDeviceConfig();
            component.setAlpha(deviceConfig.getAlpha());
            component.setBeta(deviceConfig.getBeta());
        }

        bufferSIzeText.setText(Integer.toString(config.getBufferSize()));
        requestNumberText.setText(Integer.toString(config.getRequestNumber()));
    }

    private SystemConfig createConfig() {
        StringBuilder errorMessage = new StringBuilder();

        List<SourceConfig> sourceConfigList = new ArrayList<>();
        if (sourceConfigs.getChildren().isEmpty()) {
            errorMessage.append("Not valid sources. At least one source must be provided!\n");
        }
        for (Node node : sourceConfigs.getChildren()) {
            SourceConfigComponent component = (SourceConfigComponent) node;

            try {
                double lambda = component.getLambda();
                if (lambda <= 0) {
                    errorMessage.append("Not valid lambda. Must be positive!\n");
                }
                sourceConfigList.add(new SourceConfig(lambda));
            } catch (NumberFormatException e) {
                errorMessage.append("Not valid lambda. Must be double!\n");
            }
        }

        List<DeviceConfig> deviceConfigList = new ArrayList<>();
        if (deviceConfigs.getChildren().isEmpty()) {
            errorMessage.append("Not valid devices. At least one device must be provided!\n");
        }
        for (Node node : deviceConfigs.getChildren()) {
            DeviceConfigComponent component = (DeviceConfigComponent) node;

            double alpha = 0;
            double beta = 0;
            try {
                alpha = component.getAlpha();
                if (alpha < 0) {
                    errorMessage.append("Not valid alpha. Must not be negative!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Not valid alpha. Must be double!\n");
            }
            try {
                beta = component.getBeta();
                if (beta < 0) {
                    errorMessage.append("Not valid beta. Must not be negative!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Not valid beta. Must be double!\n");
            }
            if (alpha > beta) {
                errorMessage.append("Not valid alpha and beta. Beta must be larger then alpha!\n");
            }
            deviceConfigList.add(new DeviceConfig(alpha, beta));
        }

        int buffSize = 0;
        try {
            buffSize = Integer.parseInt(bufferSIzeText.getText());
            if (buffSize < 0) {
                errorMessage.append("Not valid buffer size. Must be positive!\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Not valid buffer size. Must be integer!\n");
        }

        int requestNumber = 0;
        try {
            requestNumber = Integer.parseInt(requestNumberText.getText());
            if (requestNumber < 0) {
                errorMessage.append("Not valid request number. Must be positive!\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Not valid request number. Must be integer!\n");
        }

        if (errorMessage.length() == 0) {
            return new SystemConfig(
                    buffSize,
                    requestNumber,
                    sourceConfigList,
                    deviceConfigList
            );
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Invalid fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage.toString());

            alert.showAndWait();
            return null;
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setConfig(SystemConfig config) {
        app.setConfig(config);
        applyConfig(config);
    }
}
