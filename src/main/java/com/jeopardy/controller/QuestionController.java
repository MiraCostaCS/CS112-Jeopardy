package com.jeopardy.controller;

import com.jeopardy.model.Clue;
import com.jeopardy.model.GameModel;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the question dialog view in the Jeopardy game.
 * Manages displaying the selected clue's question, capturing the player's
 * answer input, validating the answer via the model, and triggering callbacks
 * upon completion.
 */
public class QuestionController {

    /** Label displaying the clue's question text to the player. */
    @FXML private Label questionLabel;

    /** TextField where the player enters their answer. */
    @FXML private TextField answerInput;

    /** The active clue being displayed and evaluated. */
    private Clue activeClue;

    /** The underlying game model used to evaluate answers and adjust scores. */
    private GameModel model;

    /** Callback executed after the user submits an answer to notify parent controllers. */
    private Runnable onCompleteCallback;

    /**
     * Default constructor for QuestionController.
     */
    public QuestionController() {
    }

    /**
     * Initializes the controller with the selected clue, the game model, and a completion callback.
     * Sets the question label text to display the current clue's question.
     *
     * @param clue the {@link Clue} instance containing the question and answer
     * @param model the {@link GameModel} used to check the answer and update score
     * @param onCompleteCallback a {@link Runnable} callback executed after the question is answered
     */
    public void setClueData(Clue clue, GameModel model, Runnable onCompleteCallback) {
        this.activeClue = clue;
        this.model = model;
        this.onCompleteCallback = onCompleteCallback;
        questionLabel.setText(clue.getQuestion());
    }

    /**
     * Event handler for the "Submit Answer" button.
     * Retrieves the input answer, evaluates it using the game model, displays an
     * information alert feedback dialog, triggers the completion callback, and closes the window.
     */
    @FXML
    private void handleSubmit() {
        String userAnswer = answerInput.getText();
        boolean correct = model.checkAnswer(activeClue, userAnswer);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        //alert.setResizable(true);
        alert.getDialogPane().setMinWidth(400);
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        contentLabel.setStyle("-fx-font-size: 30px;");

        if (correct) {
            alert.setTitle("Correct!");
            alert.setContentText("You earned $" + activeClue.getPoints() + "!");
        } else {
            alert.setTitle("Incorrect");
            alert.setContentText("The correct answer was: " + activeClue.getAnswer());
        }
        alert.showAndWait();

        if (onCompleteCallback != null) {
            onCompleteCallback.run();
        }

        Stage stage = (Stage) answerInput.getScene().getWindow();
        stage.close();
    }
}