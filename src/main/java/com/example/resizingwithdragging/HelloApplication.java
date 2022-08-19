package com.example.resizingwithdragging;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.NumberFormat;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // This Demo shows how to set UNDECORATED stage and be able to resize it with dragging. There is also code that allows the stage to be moved around by dragging

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        // set stage to be UNDERCORATED- Meaning
            // no control buttons at the top,
            // cant resize the window by dragging,
            // cant move the window around with dragging
            // have to implement those yourself
        stage.initStyle(StageStyle.UNDECORATED);
        // add the stage to the resize helper class which allows for the features added to the window
        // note you can move all the event handling in this resizehelper class to your stage controller for the scene and use that instead.
        ResizeHelper.addResizeListener(stage);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}