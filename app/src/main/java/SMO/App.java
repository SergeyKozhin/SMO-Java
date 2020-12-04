package SMO;

import SMO.controllers.ResultWindow;
import SMO.controllers.SettingsWindow;
import SMO.controllers.StepModeView;
import SMO.controllers.SystemConfig;
import SMO.model.system.SimulationSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;
    private SystemConfig config;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        openSettingsWindow();
        stage.show();
    }

    public void openSettingsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SMO/views/SettingsWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Settings");

            SettingsWindow controller = loader.getController();
            controller.setApp(this);
            controller.setConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openStepModeWindow(SimulationSystem system) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SMO/views/StepModeWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Step mode");

            StepModeView controller = loader.getController();
            controller.setApp(this);
            controller.setSystem(system);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openStepResultsWindow(SimulationSystem system) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/SMO/views/ResultsWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Results");

            ResultWindow controller = loader.getController();
            controller.setApp(this);
            controller.setSystem(system);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setConfig(SystemConfig config) {
        this.config = config;
    }
}
