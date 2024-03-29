package controllers;

import common.data.*;
import common.exceptions.InvalidDataException;
import common.exceptions.InvalidEnumException;
import common.exceptions.InvalidNumberException;
import controllers.tools.ObservableResourceFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.App;


public class AskWindowController {
    @FXML
    public Button backButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label impactSpeedLabel;


    @FXML
    private Label realHeroLabel;
    @FXML
    private Label hasToothpickLabel;
    @FXML
    private Label soundtrackNameLabel;
    @FXML
    private Label minutesOfWaitingLabel;
    @FXML
    private Label weaponTypeLabel;
    @FXML
    private Label carLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField impactSpeedField;
    @FXML
    private TextField soundtrackNameField;
    @FXML
    private TextField minutesOfWaitingField;

    @FXML
    private TextField carField;

    @FXML
    private ComboBox<WeaponType> weaponTypeBox;
    @FXML
    public CheckBox realHeroBox;
    @FXML
    public CheckBox hasToothpickBox;

    @FXML
    private Button enterButton;


    private Stage askStage;
    private HumanBeing resultHuman;
    private ObservableResourceFactory resourceFactory;
    private HumanBeing human;
    private App app;

    @FXML
    public void initialize() {
        askStage = new Stage();
        weaponTypeBox.setItems(FXCollections.observableArrayList(WeaponType.values()));
    }

    public String readName() throws InvalidDataException {
        String s = nameField.getText();
        if (s == null || s.equals("")) {
            throw new InvalidDataException("[NameEmptyException]");
        }
        return s;
    }

    public Car readCar() {
        String s = carField.getText();
        if (s == null || s.equals("")) {
            return null;
        }
        return new Car(s);
    }

    public double readXCoord() throws InvalidDataException {
        double x;
        try {
            x = Double.parseDouble(coordinatesXField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[XCoordFormatException]");
        }
        if (Double.isInfinite(x) || Double.isNaN(x)) throw new InvalidDataException("[XCoordFormatException]");
        return x;
    }

    public double readYCoord() throws InvalidDataException {
        double y;
        try {
            y = Double.parseDouble(coordinatesYField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[YCoordFormatException]");
        }
        if (Double.isInfinite(y) || Double.isNaN(y)) throw new InvalidDataException("[YCoordLimitException]");
        return y;
    }

    public Coordinates readCoords() throws InvalidDataException {
        double x = readXCoord();
        double y = readYCoord();
        return new Coordinates(x, y);
    }

    public boolean readRealHero() {
        return realHeroBox.isSelected();
    }

    public boolean readHasToothPick() {
        return hasToothpickBox.isSelected();
    }


    public int readImpactSpeed() throws InvalidDataException {
        int s;
        try {
            s = Integer.parseInt(impactSpeedField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[ImpactSpeedFormatException]");
        }
        if (s <= 0) throw new InvalidNumberException("[ImpactSpeedLimitException]");
        return s;
    }

    public String readSoundtrackName() throws InvalidDataException {
        String soundtrackName = soundtrackNameField.getText();
        if (soundtrackName == null || soundtrackName.equals("")) {
            throw new InvalidDataException("[SoundtrackNameEmptyException]");
        }
        return soundtrackName;
    }

    public float readMinutesOfWaiting() throws InvalidDataException {
        float minutesOfWaiting;
        try {
            minutesOfWaiting = Float.parseFloat(minutesOfWaitingField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[MinutesOfWaitingFormatException]");
        }
        if (minutesOfWaiting < 0) throw new InvalidNumberException("MinutesOfWaitingLimitException]");
        return minutesOfWaiting;
    }

    public WeaponType readWeaponType() throws InvalidEnumException {
        WeaponType weaponType = weaponTypeBox.getSelectionModel().getSelectedItem();
        if(weaponType==null) throw new InvalidEnumException("[WeaponTypeEmptyException]");
        return weaponType;
    }

    public HumanBeing readHuman() throws InvalidDataException {
        askStage.showAndWait();

        if (human == null) throw new InvalidDataException("��� ����");
        return human;

    }

    public void setHuman(HumanBeing human) {
        nameField.setText(human.getName());
        coordinatesXField.setText(human.getCoordinates().getX() + "");
        coordinatesYField.setText(human.getCoordinates().getY() + "");
        realHeroBox.setSelected(human.checkRealHero());
        hasToothpickBox.setSelected(human.checkHasToothpick());
        impactSpeedField.setText(human.getImpactSpeed() + "");
        soundtrackNameField.setText(human.getSoundtrackName());
        minutesOfWaitingField.setText(human.getMinutesOfWaiting()+ "");
        weaponTypeBox.setValue(human.getWeaponType());
        carField.setText(human.getCar().getName()!=null?human.getCar().getName():"");
    }

    @FXML
    private void backButtonOnAction() {
        askStage.close();
    }

    @FXML
    private void enterButtonOnAction() {
        try {

            String name = readName();
            Coordinates coords = readCoords();
            Boolean realHero = readRealHero();
            Boolean hasToothpick = readHasToothPick();
            Integer impactSpeed = readImpactSpeed();
            String soundtrackName = readSoundtrackName();
            Float minutesOfWaiting = readMinutesOfWaiting();
            WeaponType weaponType = readWeaponType();
            Car car = readCar();
            human = new DefaultHuman(name, coords, realHero, hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);

            askStage.close();
        } catch (InvalidDataException | IllegalArgumentException exception) {
            app.getOutputManager().error(exception.getMessage());
        }
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
        this.askStage.setOnCloseRequest((e) -> human = null);
    }

    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Init langs.
     */

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }

    public void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        realHeroLabel.textProperty().bind(resourceFactory.getStringBinding("RealHeroColumn"));
        hasToothpickLabel.textProperty().bind(resourceFactory.getStringBinding("HasToothpickColumn"));
        impactSpeedLabel.textProperty().bind(resourceFactory.getStringBinding("ImpactSpeedColumn"));
        soundtrackNameLabel.textProperty().bind(resourceFactory.getStringBinding("SoundtrackNameColumn"));
        minutesOfWaitingLabel.textProperty().bind(resourceFactory.getStringBinding("MinutesOfWaitingColumn"));
        weaponTypeLabel.textProperty().bind(resourceFactory.getStringBinding("WeaponTypeColumn"));
        carLabel.textProperty().bind(resourceFactory.getStringBinding("CarColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
        backButton.textProperty().bind(resourceFactory.getStringBinding("BackButton"));
    }
}
