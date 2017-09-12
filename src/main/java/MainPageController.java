import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController extends GridPane implements Initializable {

    private final static String pageLoc = "/resources/MainPage.fxml";

    @FXML
    private TextField fileSelectionTextField;
    @FXML
    private Button selectFileButton;
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

    public MainPageController() {
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
//        this.chooseFileBtn.setOnAction((event -> selectFile()));
//        this.newFileBtn.setOnAction((event -> openCreateFilePopup()));
//        this.loginBtn.setOnAction((event -> login()));
        this.selectFileButton.setOnAction((event -> selectFiles()));
    }

    private void selectFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(selectFileButton.getScene().getWindow());
    }
}
