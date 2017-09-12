import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

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
    private CheckBox hiddenCheckbox;
    @FXML
    private CheckBox includeSubCheckbox;
    @FXML
    private CheckBox displaySubCheckbox;
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

    private Set<File> primaryFiles;
    private Set<File> allFiles;
    private boolean renameFilesSetting;
    private boolean renameDirectoriesSetting;
    private boolean renameHiddenSetting;
    private boolean includeSubSetting;
    private boolean displaySubSetting;
    private RenamingOperation operation;
    private String textToAdd;


    public MainPageController() {
        this.primaryFiles = new TreeSet<>(new FileDepthComparator());
        this.allFiles = new TreeSet<>(new FileDepthComparator());
        this.renameFilesSetting = true;
        this.renameDirectoriesSetting = true;
        this.renameHiddenSetting = false;
        this.includeSubSetting = false;
        this.displaySubSetting = true;
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
        this.filesCheckbox.setSelected(true);
        this.directoriesCheckbox.setSelected(true);
        this.displaySubCheckbox.setDisable(true);
        this.displaySubCheckbox.setSelected(true);
        this.operationChoiceBox.setItems(FXCollections.observableArrayList(RenamingOperation.values()));
        this.operationChoiceBox.setValue(RenamingOperation.Prepend);

        this.addFilesButton.setOnAction((event -> addFiles()));
        this.addDirectoryButton.setOnAction((event -> addDirectory()));
        this.clearFileSelectionButton.setOnAction((event -> clearSelectedFiles()));
        this.filesCheckbox.setOnAction((event -> updateRenameFilesSetting()));
        this.directoriesCheckbox.setOnAction((event -> updateRenameDirectoriesSetting()));
        this.hiddenCheckbox.setOnAction((event -> updateRenameHiddenSetting()));
        this.includeSubCheckbox.setOnAction((event -> updateIncludeSubSetting()));
        this.displaySubCheckbox.setOnAction((event -> updateDisplaySubSetting()));
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
            this.primaryFiles.addAll(selectedFiles);
            computeAllFilesRecursively();
            updateSelectedFilesTextField();
        }
    }

    private void addDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(addDirectoryButton.getScene().getWindow());
        if (selectedDirectory != null) {
            this.primaryFiles.add(selectedDirectory);
            computeAllFilesRecursively();
            updateSelectedFilesTextField();
        }
    }

    private void clearSelectedFiles() {
        this.primaryFiles.clear();
        computeAllFilesRecursively();
        updateSelectedFilesTextField();
    }

    private void updateSelectedFilesTextField() {
        Set<File> files = getActiveFileSet();
        if (!displaySubSetting) {
            files = this.primaryFiles;
        }

        String hiddenString = "Hidden:\n";
        String filesString = "Files:\n";
        String directoriesString = "Directories:\n";

        for (File currentFile : files) {
            if (currentFile.isHidden()) {
                hiddenString += currentFile.getName();
                hiddenString += "\n";
            } else if (currentFile.isFile()) {
                filesString += currentFile.getName();
                filesString += "\n";
            } else if (currentFile.isDirectory()) {
                directoriesString += currentFile.getName();
                directoriesString += "\n";
            } else {
                System.err.println("WARNING: Unknown file type: " + currentFile.getName());
            }
        }

        String updatedText = "";
        if (this.renameHiddenSetting) {
            updatedText += hiddenString + "\n";
        }
        if (this.renameFilesSetting) {
            updatedText += filesString + "\n";
        }
        if (this.renameDirectoriesSetting) {
            updatedText += directoriesString;
        }

        this.fileSelectionTextArea.setText(updatedText);
    }

    private void updateRenameFilesSetting() {
        this.renameFilesSetting = this.filesCheckbox.isSelected();
    }

    private void updateRenameDirectoriesSetting() {
        this.renameDirectoriesSetting = this.directoriesCheckbox.isSelected();
    }

    private void updateRenameHiddenSetting() {
        this.renameHiddenSetting = this.hiddenCheckbox.isSelected();
    }

    private void updateIncludeSubSetting() {
        this.includeSubSetting = this.includeSubCheckbox.isSelected();
        if (includeSubSetting) {
            this.displaySubCheckbox.setDisable(false);
        } else {
            this.displaySubCheckbox.setDisable(true);
        }
        computeAllFilesRecursively();
        updateSelectedFilesTextField();
    }

    private void updateDisplaySubSetting() {
        this.displaySubSetting = this.displaySubCheckbox.isSelected();
        updateSelectedFilesTextField();
    }

    private void updateOperation() {
        this.operation = this.operationChoiceBox.getValue();
    }

    private void renameFiles() {
//        System.out.println(this.primaryFiles.toString());
//        System.out.println(this.renameFilesSetting);
//        System.out.println(this.renameDirectoriesSetting);
//        System.out.println(this.includeSubSetting);
//        System.out.println(this.operation);
//        System.out.println(this.textToAdd);
//
//        System.out.println("");
//        printFileList();

        Set<File> files = getActiveFileSet();
        for (File currentFile : files) {
            if ((currentFile.isHidden() && !this.renameHiddenSetting)
                    || (currentFile.isDirectory() && !this.renameDirectoriesSetting)
                    || (currentFile.isFile() && !this.renameFilesSetting)) {
                continue;
            }

            Path oldPath = currentFile.toPath();
            String newPathString = calculateNewPathString(currentFile.getPath());
            try {
                Files.move(currentFile.toPath(), Paths.get(newPathString));
            } catch (IOException e) {
                System.err.println("ERROR: Could not rename " + currentFile.getPath());
                e.printStackTrace();
            }
        }
    }

    private String calculateNewPathString(String oldPathString) {
        if (this.operation == RenamingOperation.Prepend) {
            int breakpoint = oldPathString.lastIndexOf("\\");
            String firstHalf = oldPathString.substring(0, breakpoint + 1);
            String lastHalf = oldPathString.substring(breakpoint + 1);
            return firstHalf + this.textToAdd + lastHalf;
        } else if (this.operation == RenamingOperation.Append) {
            int breakpoint = oldPathString.lastIndexOf(".");
            if (breakpoint != -1) {
                String firstHalf = oldPathString.substring(0, breakpoint);
                String lastHalf = oldPathString.substring(breakpoint);
                return firstHalf + this.textToAdd + lastHalf;
            } else {
                return oldPathString + this.textToAdd;
            }
        } else {
            System.err.println("ERROR: Invalid renaming operation");
            return "error";
        }
    }

    private void computeAllFilesRecursively() {
        this.allFiles.clear();
        if (this.includeSubSetting) {
            for (File currentFile : this.primaryFiles) {
                if (currentFile.isDirectory()) {
                    try (Stream<Path> paths = Files.walk(Paths.get(currentFile.toURI()))) {
                        paths.forEach((path) -> {
                            this.allFiles.add(new File(path.toString()));
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            this.allFiles.addAll(this.primaryFiles);
        }
    }

    private void printFileList() {
        Set<File> files = getActiveFileSet();
        System.out.println("Count: " + files.size());
        for (File currentFile : files) {
            System.out.println(currentFile.toString());
        }
    }

    private Set<File> getActiveFileSet() {
        if (this.includeSubSetting) {
            return this.allFiles;
        } else {
            return this.primaryFiles;
        }
    }
}
