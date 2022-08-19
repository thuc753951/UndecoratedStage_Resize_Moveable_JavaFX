module com.example.resizingwithdragging {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.resizingwithdragging to javafx.fxml;
    exports com.example.resizingwithdragging;
}