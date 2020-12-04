package SMO.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SourceConfigComponent extends HBox {
    @FXML
    private TextField lambdaTextField;
    private VBox container;

    public SourceConfigComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SMO/views/SourceConfigComponent.fxml"));
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

    public double getLambda() {
        return Double.parseDouble(lambdaTextField.getText());
    }

    public void setLambda(double lambda) {
        lambdaTextField.setText(Double.toString(lambda));
    }

    public void setContainer(VBox container) {
        this.container = container;
    }
}
