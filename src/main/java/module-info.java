module com.app {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.app to javafx.fxml;
    opens com.app.Registrasi to javafx.fxml; 
    opens com.app.Reminder to javafx.fxml; 
    opens com.app.Transfer to javafx.fxml; 
    opens com.app.Riwayat.controller to javafx.fxml;
    opens com.app.Riwayat.model to javafx.fxml;
    opens com.app.Topup to javafx.fxml;
    exports com.app;
}
