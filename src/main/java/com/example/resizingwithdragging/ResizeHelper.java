package com.example.resizingwithdragging;


import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper {

    public static ResizeListener addResizeListener (Stage stage) {

        // initiate the handler to allow the stage to detect the following MOUSE EVENTS
        ResizeListener resizeListener = new ResizeListener(stage);
        Scene scene = stage.getScene();
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);

        return resizeListener;
    }


    public static class ResizeListener implements EventHandler<MouseEvent> {
        private Stage stage;
        private Scene scene;
        private Cursor cursorEvent = Cursor.DEFAULT;

        //border is custom entered value, it denotes the border area around the window where the mouse can be dragged to resize the window.
        // currently 4 pixels from the edge x,y.
        private int border = 4; // the amount of space allocated around to indicate ur resizing

        // custom-made value, change it to be larger if you only want the window to only be resizable until a certain minimum size
        private int setMinHeight = border*2;
        private int setMinWidth = border*2;
        private double startX = 0;
        private double startY = 0;

        //private double sceneOffsetX = 0; // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED, IF YOU TAKE OUT BOTH OFFSET IT WORKS THE SAME
        //private double sceneOffsetY = 0; // might only be needed if you're doing padding, but not really since the padding is does not increase window size.

        //variables for dragging window around
        private double x = 0; //variable used for dragging window
        private double y = 0; // variable used for dragging window


        public ResizeListener (Stage stage) {
            this.stage = stage;
            this.scene = stage.getScene();
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();

            // get x and y coordinates of the mouse location.
            double mouseEventX = mouseEvent.getSceneX(),
                    mouseEventY = mouseEvent.getSceneY(),
                    // get the width and height of the application's window, minus the padding.
                    viewWidth = stage.getWidth(),
                    viewHeight = stage.getHeight();

            // this is where you. check if moused moved for resizing
            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
                //check for mouse location when pressed down and set the right cursor for resize.
                if (mouseEventX < border && mouseEventY < border) {
                    cursorEvent = Cursor.NW_RESIZE;
                }// if the mouse is in the south-west corner of the application, set cursor to South west resize
                else if (mouseEventX < border && mouseEventY > viewHeight - border) {
                    cursorEvent = Cursor.SW_RESIZE;
                }// if the mouse is  in the north-east corner, set cursor event to north-east
                else if (mouseEventX > viewWidth - border && mouseEventY < border) {
                    cursorEvent = Cursor.NE_RESIZE;
                }// if the mouse is in the south-east corner, set cursor event to south-east
                else if (mouseEventX > viewWidth - border && mouseEventY > viewHeight - border) {
                    cursorEvent = Cursor.SE_RESIZE;
                }// if the mouse is in the west side edge, set cursor event to resize west
                else if (mouseEventX < border) {
                    cursorEvent = Cursor.W_RESIZE;
                }// if the mouse is in the east side, set cursor event to resize east
                else if (mouseEventX > viewWidth - border) {
                    cursorEvent = Cursor.E_RESIZE;
                }// if the mouse is in the north side/ top of application, set cursor type to resize north
                else if (mouseEventY < border) {
                    cursorEvent = Cursor.N_RESIZE;
                }// if the mouse is in the south/ bottom of the application, set cursor type to resize south
                else if (mouseEventY > viewHeight - border) {
                    cursorEvent = Cursor.S_RESIZE;
                }// if the application is not near the edge at all set default cursor.
                else { // do nothing
                    cursorEvent = Cursor.DEFAULT;
                }

                scene.setCursor(cursorEvent);
            }
            else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) && !Cursor.DEFAULT.equals(cursorEvent)) {
                startX = viewWidth - mouseEventX;
                startY = viewHeight - mouseEventY;
                //sceneOffsetX = mouseEvent.getSceneX();
                //sceneOffsetY = mouseEvent.getSceneY(); // is the offset for where you start to drag on the window from the actual top of the application.
                // Since it's not actually precise where you dragged
                //System.out.println("mouseEvent.getSceneY= "+sceneOffsetY);
            }
            // if the mouse is dragged and not equal default
            else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && !Cursor.DEFAULT.equals(cursorEvent)) {

                if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {

                    // border*2 is used to set a min-height, if min-height is too small like 0 or not set.
                    double minHeight = stage.getMinHeight() > (setMinHeight) ? stage.getMinHeight() : (setMinHeight); // called ternary operator (? :) basically an if else condense
                    //System.out.println(minHeight);

                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) { // grabbed event on the top of the stage.
                        if (stage.getHeight() > minHeight || mouseEventY < 0) {

                            // stage.getY() - returns the Y distance from the top left of your application window to the top of the screen
                            // mouseEvent.getScreenY() - returns the mouse's Y value relative to the screen.
                            // stage.getHeight() - returns the stage's total Y value basically the height of the window.
                            // sceneOffsetY - returns the Y value of when you first pressed relative to the window.
                            // basically the distance between your mouse to the top of the window.
                            // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED, IF YOU TAKE OUT BOTH OFFSET IT WORKS THE SAME
                            double height = stage.getY() - mouseEvent.getScreenY() + stage.getHeight() /*+ sceneOffsetY*/; // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED

                            // need to set the top of the window to the height the mouse moved pr else the getY() will stay the same,
                            // with the offset from your mouse to the border.
                            double y = mouseEvent.getScreenY() /*- sceneOffsetY*/; // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED

                            stage.setHeight(height);
                            stage.setY(y);

                        }
                    } else {
                        if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {

                            stage.setHeight(mouseEventY + startY);

                        }
                    }
                }

                if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {

                    // check for if the min width of the stage or scene is set. if not just use our custom min width
                    double minWidth = stage.getMinWidth() > setMinWidth ? stage.getMinWidth() : setMinWidth;

                    // check our cursor for these cursor types before doing calculations, make sure the mouse is on the right side of the border.
                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {

                        // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED, IF YOU TAKE OUT BOTH OFFSET IT WORKS THE SAME
                        if (stage.getWidth() > minWidth || mouseEventX < 0) {

                            //calculate the new values.
                            // stage.getX - get the X's distance between the top left of your app to the top left of screen. top left is where 0,0 is.
                            // getScreenX() - different than getSceneX(), return current x value of your mouse location relative to the screen
                            // stage.getWidth - return current total width of the stage
                            double width = stage.getX() - mouseEvent.getScreenX() + stage.getWidth() /*+ sceneOffsetX*/;// IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED
                            double x = mouseEvent.getScreenX() /*- sceneOffsetX*/; // IMPORTANT NOTE: FOUND OUT LATER THE SCENE-OFFSET IS NOT NEEDED

                            // set the new values
                            stage.setWidth(width);
                            stage.setX(x);
                        }
                    } else {
                        if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {
                            stage.setWidth(mouseEventX + startX);
                        }
                    }
                }
                // pressing and then dragging the window.
            } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) && Cursor.DEFAULT.equals(cursorEvent)) {
                //once pressed, after checking for cursor type of default. set x and y values of the mouse location relative to the window
                // where the top left of the window is 0,0
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();

            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && Cursor.DEFAULT.equals(cursorEvent)) {

                // after pressing and moving to dragging the window, set the stage's position relative to the screen to where the mouse is.
                stage.getScene().getWindow().setX(mouseEvent.getScreenX() - x);
                stage.getScene().getWindow().setY(mouseEvent.getScreenY() - y);
            }
        }
    }

}
