package SMO.controllers;

import SMO.App;
import SMO.model.elements.Request;
import SMO.model.system.Event;
import SMO.model.system.SimulationSystem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StepModeView {
    private App app;
    private SimulationSystem system;
    private ObservableList<SystemState> states = FXCollections.observableArrayList();
    private ObservableList<List<Integer>> buffers = FXCollections.observableArrayList();
    private ObservableList<List<Integer>> devices = FXCollections.observableArrayList();

    @FXML
    private TableView<SystemState> eventsTable;

    @FXML
    private TableColumn<SystemState, Double> timeColumn;

    @FXML
    private TableColumn<SystemState, String> typeColumn;

    @FXML
    private TableColumn<SystemState, Integer> createdColumn;

    @FXML
    private TableColumn<SystemState, Integer> processedColumn;

    @FXML
    private TableColumn<SystemState, Integer> rejectedColumn;

    @FXML
    private TableView<List<Integer>> bufferTable;

    @FXML
    private TableView<List<Integer>> devicesTable;

    @FXML
    private ScrollPane mainScroll;

    @FXML
    private void initialize() {
        timeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getEvent().getTime()).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getType().toString()));
        createdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCreated()).asObject());
        processedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProcessed()).asObject());
        rejectedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRejected()).asObject());

        eventsTable.setItems(states);
        bufferTable.setItems(buffers);
        devicesTable.setItems(devices);

        eventsTable.prefHeightProperty().bind(eventsTable.fixedCellSizeProperty().multiply(Bindings.size(eventsTable.getItems()).add(2)));
        bufferTable.prefHeightProperty().bind(bufferTable.fixedCellSizeProperty().multiply(Bindings.size(bufferTable.getItems()).add(2)));
        devicesTable.prefHeightProperty().bind(devicesTable.fixedCellSizeProperty().multiply(Bindings.size(devicesTable.getItems()).add(2)));

        eventsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue != null) {
                int number = eventsTable.getSelectionModel().getSelectedIndex();
                bufferTable.getSelectionModel().select(number);
                devicesTable.getSelectionModel().select(number);
            } else {
                bufferTable.getSelectionModel().clearSelection();
                devicesTable.getSelectionModel().clearSelection();
            }
        });

        bufferTable.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue != null) {
                int number = bufferTable.getSelectionModel().getSelectedIndex();
                eventsTable.getSelectionModel().select(number);
                devicesTable.getSelectionModel().select(number);
            } else {
                eventsTable.getSelectionModel().clearSelection();
                devicesTable.getSelectionModel().clearSelection();
            }
        });

        devicesTable.getSelectionModel().selectedItemProperty().addListener((observableValue, item, newValue) -> {
            if (newValue != null) {
                int number = devicesTable.getSelectionModel().getSelectedIndex();
                bufferTable.getSelectionModel().select(number);
                eventsTable.getSelectionModel().select(number);
            } else {
                bufferTable.getSelectionModel().clearSelection();
                eventsTable.getSelectionModel().clearSelection();
            }
        });
    }

    public void setSystem(SimulationSystem system) {
        this.system = system;

        for (int i = 0; i < system.getBuffer().getSize(); i++) {
            addColumn(i, bufferTable);
        }

        for (int i = 0; i < system.getDevices().size(); i++) {
            addColumn(i, devicesTable);
        }
    }

    private void addColumn(int i, TableView<List<Integer>> table) {
        TableColumn<List<Integer>, String> column = new TableColumn<>(Integer.toString(i));
        int tempI = i;
        column.setCellValueFactory(cellData -> {
            int number = cellData.getValue().get(tempI);
            if (number == -1) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty(Integer.toString(number));
        });
        table.getColumns().add(column);
    }

    @FXML
    private void nextStep() {
        Event lasEvent = system.makeNextStep();
        states.add(new SystemState(
                lasEvent,
                system.getRequestCount(),
                system.getProcessed().size(),
                system.getRejected().size()
        ));

        List<Integer> buffer = new ArrayList<>(Collections.nCopies(system.getBuffer().getSize(), -1));
        for (int i = 0; i < system.getBuffer().getBuffer().size(); i++) {
            buffer.set(i, system.getBuffer().getBuffer().get(i).getNumber());
        }
        buffers.add(buffer);

        List<Integer> device = new ArrayList<>(Collections.nCopies(system.getDevices().size(), -1));
        for (int i = 0; i < system.getDevices().size(); i++) {
            Request request = system.getDevices().get(i).getCurrentRequest();
            if (request != null) {
                device.set(i, request.getNumber());
            }
        }
        devices.add(device);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mainScroll.setVvalue(1.0);
            }
        }, 50);

        eventsTable.getSelectionModel().selectLast();
    }

    @FXML
    private void onBack() {
        app.openSettingsWindow();
    }

    @FXML private void onResults() {
        app.openStepResultsWindow(system);
    }

    public void setApp(App app) {
        this.app = app;
    }
}
