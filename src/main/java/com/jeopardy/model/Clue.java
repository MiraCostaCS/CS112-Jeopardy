package com.jeopardy.model;

/**
 * Represents a single clue in a Jeopardy game, including its category,
 * point value, question, answer, and answered state.
 */
public class Clue implements Comparable<Clue> {

    // --- Default Constants ---

    /** The default category name used when none is provided. */
    public static final String DEFAULT_CATEGORY = "General";

    /** The default point value assigned to a clue. */
    public static final int DEFAULT_POINTS = 100;

    /** The default question text used when none is provided. */
    public static final String DEFAULT_QUESTION = "Default Question";

    /** The default answer text used when none is provided. */
    public static final String DEFAULT_ANSWER = "Default Answer";

    /** The default answered status assigned to a new clue. */
    public static final boolean DEFAULT_ANSWERED = false;

    // --- Instance Variables ---

    private String category;
    private int points;
    private String question;
    private String answer;
    private boolean answered;

    // --- Constructors ---

    /**
     * Constructs a {@code Clue} with default values for all fields.
     */
    public Clue() {
        this(DEFAULT_CATEGORY, DEFAULT_POINTS, DEFAULT_QUESTION, DEFAULT_ANSWER, DEFAULT_ANSWERED);
    }

    /**
     * Constructs a {@code Clue} with the specified category, point value, question,
     * answer, and answered status. Validates inputs using class setters.
     *
     * @param category the category of the clue; must not be null or blank
     * @param points   the point value of the clue; must be greater than 0
     * @param question the question prompt; must not be null or blank
     * @param answer   the correct answer; must not be null or blank
     * @param answered the initial answered state
     * @throws IllegalArgumentException if any argument fails validation
     */
    public Clue(String category, int points, String question, String answer, boolean answered) {
        if (!setCategory(category) || !setPoints(points) ||
                !setQuestion(question) || !setAnswer(answer) || !setAnswered(answered)) {
            throw new IllegalArgumentException("Invalid arguments provided to Clue constructor.");
        }
    }

    /**
     * Constructs a {@code Clue} with the specified category, point value, question,
     * and answer, defaulting the answered status to {@code false}.
     *
     * @param category the category of the clue; must not be null or blank
     * @param points   the point value of the clue; must be greater than 0
     * @param question the question prompt; must not be null or blank
     * @param answer   the correct answer; must not be null or blank
     * @throws IllegalArgumentException if any argument fails validation
     */
    public Clue(String category, int points, String question, String answer) {
        this(category, points, question, answer, DEFAULT_ANSWERED);
    }

    /**
     * Copy constructor. Creates a new {@code Clue} instance by copying fields from another.
     *
     * @param other the {@code Clue} instance to copy
     * @throws IllegalArgumentException if {@code other} is null
     */
    public Clue(Clue other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy a null Clue object.");
        }
        this.category = other.category;
        this.points = other.points;
        this.question = other.question;
        this.answer = other.answer;
        this.answered = other.answered;
    }

    // --- Accessors & Mutators ---

    /**
     * Gets the category of this clue.
     *
     * @return the clue category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this clue after trimming whitespace.
     *
     * @param category the category to set; must not be null or blank
     * @return {@code true} if the category was successfully updated; {@code false} otherwise
     */
    public boolean setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }
        this.category = category.trim();
        return true;
    }

    /**
     * Gets the point value of this clue.
     *
     * @return the point value
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the point value of this clue.
     *
     * @param points the point value to set; must be strictly positive
     * @return {@code true} if points were successfully set; {@code false} if {@code points <= 0}
     */
    public boolean setPoints(int points) {
        if (points <= 0) {
            return false;
        }
        this.points = points;
        return true;
    }

    /**
     * Gets the question text of this clue.
     *
     * @return the question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question text of this clue after trimming whitespace.
     *
     * @param question the question text to set; must not be null or blank
     * @return {@code true} if the question was successfully updated; {@code false} otherwise
     */
    public boolean setQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }
        this.question = question.trim();
        return true;
    }

    /**
     * Gets the answer text of this clue.
     *
     * @return the answer text
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer text of this clue after trimming whitespace.
     *
     * @param answer the answer text to set; must not be null or blank
     * @return {@code true} if the answer was successfully updated; {@code false} otherwise
     */
    public boolean setAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            return false;
        }
        this.answer = answer.trim();
        return true;
    }

    /**
     * Checks whether this clue has already been answered.
     *
     * @return {@code true} if answered; {@code false} otherwise
     */
    public boolean isAnswered() {
        return answered;
    }

    /**
     * Sets the answered state of this clue.
     *
     * @param answered the new answered state
     * @return {@code true}
     */
    public boolean setAnswered(boolean answered) {
        this.answered = answered;
        return true;
    }

    // --- Object Overrides ---

    /**
     * Compares this clue to the specified object for equality.
     *
     * @param other the object to compare against
     * @return {@code true} if {@code other} is a {@code Clue} with identical field values; {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Clue clue = (Clue) other;
        return points == clue.points &&
                answered == clue.answered &&
                category.equals(clue.category) &&
                question.equals(clue.question) &&
                answer.equals(clue.answer);
    }

    /**
     * Returns a string representation of this clue.
     *
     * @return a formatted string containing the clue's properties
     */
    @Override
    public String toString() {
        return "Clue{" +
                "category='" + category + '\'' +
                ", points=" + points +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", answered=" + answered +
                '}';
    }

    @Override
    public int compareTo(Clue other) {
        return this.points - other.points;
    }
}