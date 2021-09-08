package com.example.demo3.ui;

import com.example.demo3.Demo3Application;
import com.example.demo3.ui.Client.ClienteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class Principal {
    @FXML
    private MenuItem AgregarCliente;

    @FXML
    void agregarClientAction(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);

        Parent root = fxmlLoader.load(ClienteController.class.getResourceAsStream("AddClient.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Registrar un Cliente");
        stage.show();

    }

}