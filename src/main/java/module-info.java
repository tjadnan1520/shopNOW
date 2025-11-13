module com.example.product_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    opens com.example.shopnow.controller to javafx.fxml;
    exports com.example.shopnow.controller;
    opens com.example.shopnow to javafx.fxml;
    exports com.example.shopnow;
    exports com.example.shopnow.DPApply;
    opens com.example.shopnow.DPApply to javafx.fxml;
    exports com.example.shopnow.DPApply.Report;
    opens com.example.shopnow.DPApply.Report to javafx.fxml;
}