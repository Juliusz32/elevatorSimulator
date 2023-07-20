module com.example.finalnyprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.finalnyprojekt to javafx.fxml;
    exports com.example.finalnyprojekt;
}