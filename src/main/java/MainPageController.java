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
    private ChoiceBox<String> renamingOperationChoicebox;
    @FXML
    private TextField textToAddTextField;
    @FXML
    private Button renameFilesButton;
    @FXML
    private Button exitButton;

    private List<File> files;
    private List<File> directories;

    public MainPageController() {
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();

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
        this.addFilesButton.setOnAction((event -> addFiles()));
        this.addDirectoryButton.setOnAction((event -> addDirectory()));
        this.clearFileSelectionButton.setOnAction((event -> clearSelectedFiles()));

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
}
