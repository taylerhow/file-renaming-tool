import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController extends GridPane implements Initializable {

    private final static String pageLoc = "/resources/MainPage.fxml";

    @FXML
    private TextArea fileSelectionTextArea;
    @FXML
    private Button addFilesButton;
    @FXML
    private Button addDirectoryButton;
    @FXML
    private Button clearFileSelectionButton;
    @FXML
    private CheckBox directoriesCheckbox;
    @FXML
    private CheckBox filesCheckbox;
    @FXML
    private CheckBox includeSubdirectoriesCheckbox;
    @FXML
    private ChoiceBox<RenamingOperation> operationChoiceBox;
    @FXML
    private TextField textToAddTextField;
    @FXML
    private Button renameFilesButton;
    @FXML
    private Button exitButton;

    private enum RenamingOperation {
        Prepend,
        Append;
    }

    private List<File> files;
    private List<File> directories;
    private boolean renameFilesSetting;
    private boolean renameDirectoriesSetting;
    private boolean includeSubdirectoriesSetting;
    private RenamingOperation operation;
    private String textToAdd;


    public MainPageController() {
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
        this.renameFilesSetting = false;
        this.renameDirectoriesSetting = false;
        this.includeSubdirectoriesSetting = false;
        this.operation = RenamingOperation.Prepend;
        this.textToAdd = "";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageLoc));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Sets up action listeners for the buttons on the page.
     * <p>
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param fxmlFileLocation The location used to resolve relative paths for the root object, or
     *                         <tt>null</tt> if the location is not known.
     * @param resources        The resources used to localize the root object, or <tt>null</tt> if
     *                         the root object was not localized.
     */
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        this.fileSelectionTextArea.setEditable(false);
        this.operationChoiceBox.setItems(FXCollections.observableArrayList(RenamingOperation.values()));
        this.operationChoiceBox.setValue(RenamingOperation.Prepend);

        this.addFilesButton.setOnAction((event -> addFiles()));
        this.addDirectoryButton.setOnAction((event -> addDirectory()));
        this.clearFileSelectionButton.setOnAction((event -> clearSelectedFiles()));
        this.filesCheckbox.setOnAction((event -> updateRenameFilesSetting()));
        this.directoriesCheckbox.setOnAction((event -> updateRenameDirectoriesSetting()));
        this.includeSubdirectoriesCheckbox.setOnAction((event -> updateIncludeSubdirectoriesSetting()));
        this.operationChoiceBox.setOnAction((event -> updateOperation()));
        this.textToAddTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            this.textToAdd = newValue;
        }));
        this.renameFilesButton.setOnAction((event -> renameFiles()));
        this.exitButton.setOnAction((event -> Platform.exit()));

        updateSelectedFilesTextField();
    }

    private void addFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(addFilesButton.getScene().getWindow());
        if (selectedFiles != null) {
            this.files.addAll(selectedFiles);
            updateSelectedFilesTextField();
        }
    }

    private void addDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(addDirectoryButton.getScene().getWindow());
        if (selectedDirectory != null) {
            this.directories.add(selectedDirectory);
            updateSelectedFilesTextField();
        }
    }

    private void clearSelectedFiles() {
        this.files.clear();
        this.directories.clear();
        updateSelectedFilesTextField();
    }

    private void updateSelectedFilesTextField() {
        String updatedString = "Files:\n";

        for (int i = 0; i < this.files.size(); i++) {
            File currentFile = this.files.get(i);
            updatedString += currentFile.getName();
            updatedString += "\n";
        }

        updatedString += "\nDirectories:\n";
        for (int i = 0; i < this.directories.size(); i++) {
            File currentFile = this.directories.get(i);
            updatedString += currentFile.getName();
            updatedString += "\n";
        }

        this.fileSelectionTextArea.setText(updatedString);
    }

    private void updateRenameFilesSetting() {
        this.renameFilesSetting = this.filesCheckbox.isSelected();
    }

    private void updateRenameDirectoriesSetting() {
        this.renameDirectoriesSetting = this.directoriesCheckbox.isSelected();
    }

    private void updateIncludeSubdirectoriesSetting() {
        this.includeSubdirectoriesSetting = this.includeSubdirectoriesCheckbox.isSelected();
    }

    private void updateOperation() {
        this.operation = this.operationChoiceBox.getValue();
    }

    private void renameFiles() {
        // TODO: Implement
        System.out.println(this.files.toString());
        System.out.println(this.directories.toString());
        System.out.println(this.renameFilesSetting);
        System.out.println(this.renameDirectoriesSetting);
        System.out.println(this.includeSubdirectoriesSetting);
        System.out.println(this.operation);
        System.out.println(this.textToAdd);
    }
}
