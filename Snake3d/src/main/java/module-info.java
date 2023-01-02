module com.example.snake3d {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.snake3d to javafx.fxml;
    exports com.example.snake3d;
}