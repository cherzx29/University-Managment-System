module com.example.authenticationframes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.authenticationframes to javafx.fxml;
    exports com.example.authenticationframes;
}