package SMO.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DeviceConfigComponent extends HBox {
    @FXML
    private TextField alphaTextField;
    @FXML
    private TextField betaTextField;

    private VBox container;

    public DeviceConfigComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SMO/views/DeviceConfigComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @FXML
    private void onDelete() {
        container.getChildren().remove(this);
    }

    public double getAlpha() {
        return Double.parseDouble(alphaTextField.getText());
    }

    public void setAlpha(double lambda) {
        alphaTextField.setText(Double.toString(lambda));
    }

    public double getBeta() {
        return Double.parseDouble(betaTextField.getText());
    }

    public void setBeta(double lambda) {
        betaTextField.setText(Double.toString(lambda));
    }

    public void setContainer(VBox container) {
        this.container = container;
    }
}
