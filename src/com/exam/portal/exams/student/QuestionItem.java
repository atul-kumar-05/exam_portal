package com.exam.portal.exams.student;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionItem implements Initializable {
    @FXML
    VBox vBox;
    @FXML
    Text text;

    public void setText(String t){
       // vBox.getChildren().remove(imageView);
        text.setText(t);
    }

    public void setImageView(Image image){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
        imageView.setImage(image);
        vBox.getChildren().add(imageView);
        vBox.setVgrow(imageView, Priority.ALWAYS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i=0;i<10;i++){
            File file = new File("C:\\Users\\hp\\Pictures\\Screenshots\\Screenshot (134).png");
            Image image = new Image(file.toURI().toString());
            setImageView(image);
        }
    }
}
