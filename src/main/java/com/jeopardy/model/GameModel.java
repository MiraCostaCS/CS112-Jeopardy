package com.jeopardy.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the core game model for the Jeopardy application.
 * Manages game state, including storing clues, calculating player scores,
 * parsing CSV files, and organizing clues by category.
 * NOTE: this model class deviates from course conventions because we do not
 * want to allow a certain level of control (like "overriding" score by
 * using a {@code setScore} method call)
 */
public class GameModel {
    /** List storing all game clues loaded from the CSV file. */
    private final ArrayList<Clue> clueList;

    /** The player's current accumulated score. */
    private int score;

    /**
     * Default constructor for GameModel.
     * Initializes an empty game model instance.
     */
    public GameModel() {
        clueList = new ArrayList<Clue>();
        score = 0;
    }

    /**
     * Reads a CSV file and populates the clue list with new {@link Clue} objects.
     * Clears any existing clues and resets the player score to zero before loading.
     *
     * @param file the CSV {@link File} containing clue records (Category, Points, Question, Answer)
     * @throws IOException if an error occurs while reading the file
     * @throws NumberFormatException if the points column contains an unparseable integer
     */
    public void loadCSV(File file) throws IOException, NumberFormatException {
        clueList.clear();
        score = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Split respecting quoted strings with commas
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 4) {
                    String category = parts[0].trim().replace("\"", "");
                    int points = Integer.parseInt(parts[1].trim());
                    String question = parts[2].trim().replace("\"", "");
                    String answer = parts[3].trim().replace("\"", "");

                    Clue clue = new Clue(category, points, question, answer);
                    clueList.add(clue);
                }
            }
        }
    }

    /**
     * Checks the user's submitted answer against the correct clue answer.
     * Marks the clue as answered and updates the total game score accordingly.
     *
     * @param clue the {@link Clue} being answered
     * @param userAnswer the input answer provided by the player
     * @return {@code true} if the answer is correct (case-insensitive); {@code false} otherwise
     */
    public boolean checkAnswer(Clue clue, String userAnswer) {
        clue.setAnswered(true);
        boolean isCorrect = userAnswer != null && userAnswer.trim().equalsIgnoreCase(clue.getAnswer().trim());
        if (isCorrect) {
            score += clue.getPoints();
        } else {
            score -= clue.getPoints();
        }
        return isCorrect;
    }

    /**
     * Extracts unique category names from the clue list in order of appearance.
     *
     * @return an {@link ArrayList} of unique category strings
     */
    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Clue clue : clueList) {
            if (!categories.contains(clue.getCategory())) {
                categories.add(clue.getCategory());
            }
        }
        return categories;
    }

    /**
     * Filters and returns all clues belonging to a specific category.
     *
     * @param category the category string to filter clues by (case-insensitive)
     * @return an {@link ArrayList} of {@link Clue} objects matching the specified category
     */
    public ArrayList<Clue> getCluesByCategory(String category) {
        ArrayList<Clue> categoryClues = new ArrayList<>();
        for (Clue clue : clueList) {
            if (clue.getCategory().equalsIgnoreCase(category)) {
                categoryClues.add(clue);
            }
        }
        return categoryClues;
    }

    /**
     * Gets the full list of loaded clues.
     *
     * @return an {@link ArrayList} containing all {@link Clue} instances in the game
     */
    public ArrayList<Clue> getClueList() {
        return clueList;
    }

    /**
     * Gets the player's current score.
     *
     * @return the total score as an integer
     */
    public int getScore() {
        return score;
    }
}