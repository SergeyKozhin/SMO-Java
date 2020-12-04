package SMO.controllers;

import SMO.App;
import SMO.model.elements.Request;
import SMO.model.system.Event;
import SMO.model.system.EventType;
import SMO.model.system.SimulationSystem;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;


public class ResultWindow {
    private App app;

    @FXML
    private TableView<SourceStatistics> sourcesTable;

    @FXML
    private TableColumn<SourceStatistics, Integer> sourceNumberColumn;

    @FXML
    private TableColumn<SourceStatistics, Integer> createdColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> pRejectColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> tSystemColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> tWaitColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> dWaitColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> tProcessColumn;

    @FXML
    private TableColumn<SourceStatistics, Double> dProcessColumn;

    @FXML
    private TableView<DeviceStatistics> devicesTable;

    @FXML
    private TableColumn<DeviceStatistics, Integer> deviceNumberColumn;

    @FXML
    private TableColumn<DeviceStatistics, Double> kColumn;

    @FXML
    private Label pRejectLabel;

    @FXML
    private Label tSystemLabel;

    @FXML
    private Label kLabel;

    @FXML
    private void initialize() {
        sourceNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());
        createdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCreated()).asObject());
        pRejectColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPReject()).asObject());
        tSystemColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTSystem()).asObject());
        tWaitColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTWait()).asObject());
        dWaitColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDWait()).asObject());
        tProcessColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTProcess()).asObject());
        dProcessColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDProcess()).asObject());

        deviceNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());
        kColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getK()).asObject());
    }

    public void setSystem(SimulationSystem system) {
        Event lastEvent = system.makeNextStep();
        while (lastEvent.getType() != EventType.SIMULATION_FINISHED) {
            lastEvent = system.makeNextStep();
        }


        List<SourceStatistics> sources = new ArrayList<>();
        for (int i = 0; i < system.getSourcesCount(); i++) {
            int finalI = i;
            List<Request> sourceProcessed = system.getProcessed().stream().filter(r -> r.getSourceNumber() == finalI).collect(Collectors.toList());
            List<Request> sourceRejected = system.getRejected().stream().filter(r -> r.getSourceNumber() == finalI).collect(Collectors.toList());

            List<Request> sourceCreated = Stream.concat(sourceProcessed.stream(), sourceRejected.stream()).collect(Collectors.toList());

            double tSystem = sourceCreated.stream().mapToDouble(r -> r.getReleaseTime() - r.getCreationTime()).average().orElse(Double.NaN);

            double[] waitTime = DoubleStream.concat(
                    sourceProcessed.stream().mapToDouble(r -> r.getDeviceTime() - r.getCreationTime()),
                    sourceRejected.stream().mapToDouble(r -> r.getReleaseTime() - r.getCreationTime())
            ).toArray();
            double tWait = Arrays.stream(waitTime).average().orElse(Double.NaN);
            double dWait = Arrays.stream(waitTime).map(t -> Math.pow((t - tWait), 2)).average().orElse(Double.NaN);

            double[] processTime = sourceProcessed.stream().mapToDouble(r -> r.getReleaseTime() - r.getDeviceTime()).toArray();
            double tProcess = Arrays.stream(processTime).average().orElse(Double.NaN);
            double dProcess = Arrays.stream(processTime).map(t -> Math.pow((t - tProcess), 2)).average().orElse(Double.NaN);

            sources.add(new SourceStatistics(
                    i,
                    sourceCreated.size(),
                    sourceRejected.size() * 1.0 / sourceCreated.size(),
                    tSystem,
                    tWait,
                    dWait,
                    tProcess,
                    dProcess
            ));
        }
        sourcesTable.setItems(FXCollections.observableArrayList(sources));

        List<DeviceStatistics> devices = new ArrayList<>();
        for (int i = 0; i < system.getDevices().size(); i++) {
            int finalI = i;
            double k = system.getProcessed().stream()
                    .filter(r -> r.getDeviceNumber() == finalI)
                    .mapToDouble(r -> r.getReleaseTime() - r.getDeviceTime())
                    .sum() / lastEvent.getTime();

            devices.add(new DeviceStatistics(i, k));
        }
        devicesTable.setItems(FXCollections.observableArrayList(devices));

        double pReject =
                sources.stream().mapToDouble(s -> s.getCreated() * s.getPReject()).sum() /
                        sources.stream().mapToDouble(SourceStatistics::getCreated).sum();

        double tSystem =
                sources.stream().mapToDouble(s -> s.getCreated() * s.getTSystem()).sum() /
                        sources.stream().mapToDouble(SourceStatistics::getCreated).sum();

        double k = devices.stream().mapToDouble(DeviceStatistics::getK).average().orElse(Double.NaN);

        pRejectLabel.setText(Double.toString(pReject));
        tSystemLabel.setText(Double.toString(tSystem));
        kLabel.setText(Double.toString(k));
    }

    @FXML
    private void onSettings() {
        app.openSettingsWindow();
    }

    public void setApp(App app) {
        this.app = app;
    }
}
