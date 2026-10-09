package com.jeopardy.controller;

import com.jeopardy.model.Clue;
import com.jeopardy.model.GameModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class for the main Jeopardy game board view.
 * Manages user interactions with the board, CSV file loading,
 * dynamic grid population, score updates, and launching question modal dialogs.
 */
public class MainController {

    /** Label that displays the player's current game score. */
    @FXML private Label scoreLabel;

    /** GridPane layout container used to render category headers and question buttons. */
    @FXML private GridPane boardGrid;

    /** The underlying game model managing data state and score logic. */
    private final GameModel model = new GameModel();

    /**
     * Default constructor for MainController.
     */
    public MainController() {
    }

    /**
     * Event handler for the "Load CSV File" button.
     * Opens a {@link FileChooser} dialog allowing the user to select a CSV trivia file,
     * parses the file into the model, and updates the UI game board.
     */
    @FXML
    private void handleLoadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Jeopardy CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
        );

        Stage stage = (Stage) boardGrid.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                model.loadCSV(selectedFile);
                updateScoreDisplay();
                buildBoard();
            } catch (IOException ioe) {
                showAlert("Error", "Failed to load CSV: " + ioe.getMessage());
            } catch (NumberFormatException nfe) {
                showAlert("Error", "CSV data incorrectly formatted: " + nfe.getMessage());
            }
        }
    }

    /**
     * Clears and dynamically constructs the Jeopardy game board grid.
     * Creates disabled header buttons for each category and interactive buttons
     * for clues, sorted in ascending order by point value.
     */
    private void buildBoard() {
        boardGrid.getChildren().clear();

        ArrayList<String> categories = model.getCategories();
        int col = 0;

        for (String category : categories) {
            // Category Header Button
            Button catHeader = new Button(category.toUpperCase());
            catHeader.setPrefSize(160, 60);
            catHeader.setStyle("-fx-background-color: #060CE9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            catHeader.setDisable(true);
            boardGrid.add(catHeader, col, 0);

            ArrayList<Clue> clues = model.getCluesByCategory(category);
            clues.sort(null);

            int row = 1;
            for (Clue clue : clues) {
                Button clueBtn = new Button("$" + clue.getPoints());
                clueBtn.setPrefSize(160, 80);
                clueBtn.setStyle("-fx-background-color: #060CE9; -fx-text-fill: #D6A800; -fx-font-size: 20px; -fx-font-weight: bold;");

                clueBtn.setOnAction(e -> openQuestionDialog(clue, clueBtn));

                boardGrid.add(clueBtn, col, row);
                row++;
            }
            col++;
        }
    }

    /**
     * Opens a modal popup window displaying the question associated with a selected clue.
     * Updates the board button state and total score display upon answer submission.
     *
     * @param clue the {@link Clue} instance containing question and answer details
     * @param clueBtn the board {@link Button} corresponding to the selected clue
     */
    private void openQuestionDialog(Clue clue, Button clueBtn) {
        if (clue.isAnswered()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jeopardy/fxml/question-view.fxml"));
            Parent root = loader.load();

            QuestionController controller = loader.getController();
            controller.setClueData(clue, model, () -> {
                updateScoreDisplay();
                clueBtn.setText("");
                clueBtn.setDisable(true);
                clueBtn.setStyle("-fx-background-color: #000533;");
            });

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Category: " + clue.getCategory() + " - $" + clue.getPoints());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(boardGrid.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Could not load question view FXML.");
        }
    }

    /**
     * Updates the score label text to reflect the player's current score from the model.
     */
    private void updateScoreDisplay() {
        scoreLabel.setText("Score: $" + model.getScore());
    }

    /**
     * Displays a simple information alert dialog to the user.
     *
     * @param title the title string for the alert window
     * @param message the body text message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}