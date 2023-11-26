module com.example.psp6newtry {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.psp6newtry to javafx.fxml;
    exports com.example.psp6newtry;
}