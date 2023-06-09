package com.progetto.packController;

import com.progetto.packModel.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class ControllerRegistrazione implements Initializable {
    boolean isShowPwd = false;
    private static Collection<String> c = new ArrayList<>();
    final Model model = Model.getInstance();

    @FXML
    TextField cfUtente;
    @FXML
    RadioButton showPwd = new RadioButton();

    @FXML
    ChoiceBox<String> choiceBox = new ChoiceBox<>();

    @FXML
    TextField cittaUtente = new TextField();

    @FXML
    TextField cognomeUtente = new TextField();

    @FXML
    Button confermabutton;

    @FXML
    DatePicker dataNascita = new DatePicker();

    @FXML
    TextField hiddenTextfield;

    @FXML
    TextField mailUtente = new TextField();

    @FXML
    TextField nomeUtente = new TextField();

    @FXML
    PasswordField passwordUtente = new PasswordField();

    @FXML
    TextField statoUtente = new TextField();
    @FXML
    GridPane gridPane = new GridPane();
    @FXML
    TextField tessSanitaria = new TextField();

    @FXML
    void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/first-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void checkInputs(ActionEvent actionEvent) throws SQLException, NoSuchAlgorithmException {
        c = this.model.checkRegistrazione(nomeUtente.getText(), cognomeUtente.getText(), dataNascita.getValue(),
                cittaUtente.getText(), statoUtente.getText(), cfUtente.getText(), tessSanitaria.getText(), mailUtente.getText(),
                passwordUtente.getText(), String.valueOf(choiceBox.getValue()));
        if (c.size() != 0) {
            startAlert();
        } else if (c.size() == 0 && this.model.isStatusAnagrafica() && !this.model.isTabellaCittadino()) {
            startSuccessAlert();
            this.model.insertDatiCittadino(nomeUtente.getText(), cognomeUtente.getText(), dataNascita.getValue(),
                    cittaUtente.getText(), tessSanitaria.getText(), statoUtente.getText(), cfUtente.getText(), mailUtente.getText(),
                    passwordUtente.getText(), String.valueOf(choiceBox.getValue()));

        } else if (c.size() == 0 && !this.model.isStatusAnagrafica()) {
            startAnagraficaAlert();
        } else if (c.size() == 0 && this.model.isStatusAnagrafica() && this.model.isTabellaCittadino()) {
            startCittadinoAlert();
        }
    }

    void startCittadinoAlert() {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Mancata registrazione");
        warningAlert.setHeaderText("Dati già presenti");
        warningAlert.setContentText("Sei già iscritto al portale. Se tale informazione non dovesse essere corretta contatta la nostra mail per" +
                " segnalare questo fatto.\n\tMail: info@info.info");
        warningAlert.showAndWait();
        confermabutton.setVisible(false);
        gridPane.setMouseTransparent(true);
    }

    void startAnagraficaAlert() {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Mancata registrazione");
        warningAlert.setHeaderText("Dati non presenti");
        warningAlert.setContentText("Non sei presente nella scheda anagrafica della questura.\nContatta la nostra mail per" +
                " segnalare questo fatto.\n\tMail: info@info.info");
        warningAlert.showAndWait();
        confermabutton.setVisible(false);
        gridPane.setMouseTransparent(true);
    }

    void startAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Errore");
        errorAlert.setHeaderText("Input non valido");
        String listaErr = c.toString();
        listaErr = listaErr.replace("[", "");
        listaErr = listaErr.replace("]", "");

        if (c.size() == 1) {
            errorAlert.setContentText("Controlla il campo " + listaErr + " e inserisci nuovamente il valore.");
            errorAlert.showAndWait();
        } else if (c.size() == 1 && c.contains("Data")) {
            errorAlert.setContentText("Controlla i campi " + listaErr + " e inserisci nuovamente il valore.\n\n" +
                    "Ricordati: devi essere maggiorenne e/o inserire una data di nascita valida.");
            errorAlert.showAndWait();
            errorAlert.setContentText("");
        } else if (c.size() == 1 && c.contains("Mail")) {
            errorAlert.setContentText("Controlla i campi " + listaErr + " e inserisci nuovamente il valore.\n\n" +
                    "Ricordati: devi inserire una mail valida.");
            errorAlert.showAndWait();
            errorAlert.setContentText("");
        } else if (c.size() == 1 && c.contains("Codice Fiscale")) {
            errorAlert.setContentText("Controlla i campi " + listaErr + " e inserisci nuovamente il valore.\n\n" +
                    "Il codice fiscale fornito non risulta corretto.");
            errorAlert.showAndWait();
            errorAlert.setContentText("");
        } else {
            errorAlert.setContentText("Controlla i campi " + listaErr + " e inserisci nuovamente i valori.");
            errorAlert.showAndWait();
            errorAlert.setContentText("");
        }
        c.removeAll(c);
        confermabutton.setVisible(true);
    }

    void startSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Successo");
        successAlert.setHeaderText("Operazione completata");
        successAlert.setContentText("Torna indietro alla schermata iniziale e accedi come cittadino");
        successAlert.showAndWait();
        confermabutton.setVisible(false);
        gridPane.setMouseTransparent(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> cat = FXCollections.observableArrayList("Cittadino italiano", "Cittadino Unione Europea (UE) e dello Spazio Economico Europeo(SEE)",
                "Cittadino straniero residente in Italia", "Cittadino minorenne"
                , "Cittadino apolide e rifugiato", "Cittadino straniero senza residenza in Italia", "Altro");
        choiceBox.setItems(cat);
        showPwd.setOnAction(e -> checkShowPwd());
    }

    void checkShowPwd() {
        isShowPwd = !isShowPwd;
        if(isShowPwd){ showHiddenPwd();}else{ hideHiddenPwd();}
    }

    void hideHiddenPwd() {
        hiddenTextfield.setVisible(false);
        hiddenTextfield.toBack();
        passwordUtente.toFront();
        passwordUtente.setText(hiddenTextfield.getText());
        passwordUtente.setVisible(true);
    }

    private void showHiddenPwd() {
        hiddenTextfield.toFront();
        hiddenTextfield.setVisible(true);
        passwordUtente.toBack();
        passwordUtente.setVisible(false);
        hiddenTextfield.setText(passwordUtente.getText());
    }
}