package controller;
import algorithm.ImageDataManager;
import algorithm.TrackingData;
import algorithm.TrackingService;
import algorithm.VisualizationManager;
import inputOutput.VideoSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomTargetController implements Controller {

    @FXML
    public Label trackingDataLabel;
    @FXML
    public ImageView imageView;
    @FXML
    private ListView<String> targetsListView;

    private boolean videoConnected = false;
    private ImageDataManager imageDataManager = new ImageDataManager();
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerController();
        ObservableList<String> targetItems = FXCollections.observableArrayList(
                VisualizationManager.getTargets()
                        .stream()
                        .map(Object::toString) // calls toString() on each target
                        .toList()
        );
        targetsListView.setItems(targetItems);
    }

    @Override
    public void close() {
        unregisterController();
    }

}
