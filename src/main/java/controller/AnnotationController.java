package controller;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class AnnotationController implements Controller {
    @FXML
    public VBox uploadedImages;
    @FXML
    public Button uploadImagesButton;
    @FXML
    public ScrollPane selectedImagePane;
    @FXML
    private ImageView selectedImageView;
    private ImageView currentSelectedImageView;

    private List<File> selectedImages; //commentsdw

    private List<ImageView> selectedImageViews = new List<ImageView>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<ImageView> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(ImageView imageView) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends ImageView> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends ImageView> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public ImageView get(int index) {
            return null;
        }

        @Override
        public ImageView set(int index, ImageView element) {
            return null;
        }

        @Override
        public void add(int index, ImageView element) {

        }

        @Override
        public ImageView remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<ImageView> listIterator() {
            return null;
        }

        @Override
        public ListIterator<ImageView> listIterator(int index) {
            return null;
        }

        @Override
        public List<ImageView> subList(int fromIndex, int toIndex) {
            return List.of();
        }
    }; //For clicked on images in side menu

    private boolean shift = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code goes here
    }

    @FXML
    @Override
    public void close() {
        unregisterController();
    }

    @FXML
    public void Handle_Upload_Functionality(ActionEvent actionEvent) {
        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Images");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp")
            );
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            this.selectedImages = fileChooser.showOpenMultipleDialog(currentStage);

            if (selectedImages != null) {
                for (File file : selectedImages) {
                    displayImage(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(File file) {
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        imageView.setOnMouseClicked(event -> {
            if (event.isAltDown()) {
                selectImage(image, imageView, true);
            }
            else {
                selectImage(image, imageView, false);
            }
        });

        uploadedImages.getChildren().add(imageView);
    }

    private void selectImage(Image image, ImageView imageView, Boolean multi) {

        if (selectedImageView != null && currentSelectedImageView != imageView) {
            selectedImageView.setImage(image);
            selectedImageView.setFitWidth(selectedImageView.getScene().getWidth());
            selectedImageView.setPreserveRatio(true);

            selectedImageView.getTransforms().clear();
            // Create a new Scale transformation for the ImageView
            Scale scale = new Scale();
            selectedImageView.getTransforms().add(scale);

            // Add a ScrollEvent handler to the ScrollPane
            this.selectedImagePane.addEventFilter(ScrollEvent.ANY, event -> {
                if (event.isControlDown()) {


                    // Adjust the pivot points to the mouse's current position
                    scale.setPivotX(event.getX());
                    scale.setPivotY(event.getY());

                    double zoomFactor = 1.05;

                    if (event.getDeltaY() > 0) {
                        // Zoom in
                        scale.setX(scale.getX() * zoomFactor);
                        scale.setY(scale.getY() * zoomFactor);
                    } else {
                        // Zoom out, but do not go below a certain minimum value
                        if (scale.getX() > 1.0 && scale.getY() > 1.0) {
                            scale.setX(scale.getX() / zoomFactor);
                            scale.setY(scale.getY() / zoomFactor);
                        }
                    }

                    event.consume();
                }
            });

            if(!multi && !selectedImageViews.isEmpty()){
                selectedImageViews.clear();
                for (Node iV : uploadedImages.getChildren()) {
                    iV.setStyle("");
                }
                System.out.println("multiple cleared");
            }

            if (currentSelectedImageView != null){
                selectedImageViews.add(imageView);
                //currentSelectedImageView.setStyle("");
            }
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
            currentSelectedImageView = imageView;
        }


    }

    public void Select_Next_Image(ActionEvent actionEvent) {
        try {
            if (currentSelectedImageView != null) {
                int currentIndex = uploadedImages.getChildren().indexOf(currentSelectedImageView);
                if (currentIndex < uploadedImages.getChildren().size() - 1) {
                    ImageView nextImageView = (ImageView) uploadedImages.getChildren().get(currentIndex + 1);
                    Image image = nextImageView.getImage();
                    selectImage(image, nextImageView, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Select_Previous_Image(ActionEvent actionEvent) {
        try {
            if (currentSelectedImageView != null) {
                int currentIndex = uploadedImages.getChildren().indexOf(currentSelectedImageView);
                if (currentIndex > 0) {
                    ImageView previousImageView = (ImageView) uploadedImages.getChildren().get(currentIndex - 1);
                    Image image = previousImageView.getImage();
                    selectImage(image, previousImageView, false);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
