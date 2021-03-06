package com.example.demo3.ui;


import com.example.demo3.Demo3Application;
import com.example.demo3.ui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class ApplicationJFX extends Application {

    private Parent root;

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Demo3Application.getContext()::getBean);
        root = fxmlLoader.load(MainController.class.getResourceAsStream("Main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Icon.png"));
        primaryStage.show();
    }

    @Override
    public void stop() {

        Demo3Application.getContext().close();
    }



}
