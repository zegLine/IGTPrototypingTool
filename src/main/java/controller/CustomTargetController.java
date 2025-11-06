package controller;
import algorithm.*;
import inputOutput.VideoSource;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import shapes.Target;
import util.Vector3D;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomTargetController implements Controller {

    @FXML
    public Label trackingDataLabel;
    @FXML
    public Button startToolAugmentationButton;
    @FXML
    public Label augmentationLabel;
    @FXML
    public Label dataSourceWarningLabel;
    @FXML
    private ListView<String> targetsListView;
    @FXML
    private ListView<String> toolsListView;

    private final TrackingService trackingService = TrackingService.getInstance();

    private List<Target> backingTargets;
    private List<TrackingTool> backingTools;

    private Target selectedTarget;
    private TrackingTool selectedTool;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerController();

        backingTargets = VisualizationManager.getTargets();
        ObservableList<String> targetItems = FXCollections.observableArrayList(
                backingTargets.stream().map(Object::toString).toList()
        );
        targetsListView.setItems(targetItems);

        try {
            backingTools = trackingService.getDataService()
                    .getDataManager()
                    .getToolMeasures();

            ObservableList<String> toolItems = FXCollections.observableArrayList(
                    backingTools.stream().map(TrackingTool::getName).toList()
            );
            toolsListView.setItems(toolItems);

        } catch (NullPointerException e) {
            dataSourceWarningLabel.setVisible(true);
            dataSourceWarningLabel.setDisable(false);
        }

        Timeline timeline = trackingService.getTimeline();
        if (timeline != null) {
            timeline.currentTimeProperty().addListener((obs, oldTime, newTime) -> updateAugmentationLabel());
        }
    }

    @FXML
    private void onSelectTargetClicked() {
        int idx = targetsListView.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < backingTargets.size()) {
            selectedTarget = backingTargets.get(idx);
            updateAugmentationLabel();
        }
    }

    @FXML
    private void onSelectToolClicked() {
        int idx = toolsListView.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < backingTools.size()) {
            selectedTool = backingTools.get(idx);
            updateAugmentationLabel();
        }
    }

    private double[] calculateDistances() {
        if (selectedTool == null || selectedTarget == null ||
                selectedTool.getPos() == null || selectedTarget.getPos() == null) {
            return new double[]{0.0, 0.0, 0.0};
        }

        Vector3D toolPos = selectedTool.getPos();
        Vector3D targetPos = selectedTarget.getPos();

        double dx = toolPos.getX() - targetPos.getX();
        double dy = toolPos.getY() - targetPos.getY();
        double dz = toolPos.getZ() - targetPos.getZ();

        return new double[]{dx, dy, dz};
    }

    private void updateAugmentationLabel() {
        double[] dist = calculateDistances();

        String base = String.format("""
            ΔX: %.3f
            ΔY: %.3f
            ΔZ: %.3f
            """, dist[0], dist[1], dist[2]);

        String targetLine = "Target: " + (selectedTarget != null ? selectedTarget.toString() : "—");
        String toolLine   = "Tool: "   + (selectedTool   != null ? selectedTool.getName()    : "—");
        String toolPosLine = "POS: "   + (selectedTool   != null ? selectedTool.getPosString() : "—");

        augmentationLabel.setText(base + "\n" + targetLine + "\n" + toolLine + "\n" + toolPosLine);
    }


    @Override
    public void close() {
        unregisterController();
    }

}
