package model.enums;

public enum Difficulty {
    EASY(1, 4, 5),
    MEDIUM(2, 8, 3),
    HARD(3, 12, 2);

    private int maxTexts;
    private int maxQuestions;
    private int readTimePerFile;

    Difficulty(int maxTexts, int maxQuestions, int readTimePerFile) {

        this.maxTexts = maxTexts;
        this.maxQuestions = maxQuestions;
        this.readTimePerFile = readTimePerFile;
    }

    public int getMaxTexts() { return maxTexts; }

    public void setMaxTexts(int maxTexts) { this.maxTexts = maxTexts; }

    public int getMaxQuestions() { return maxQuestions; }

    public void setMaxQuestions(int maxQuestions) { this.maxQuestions = maxQuestions; }

    public int getReadTimePerFile() { return readTimePerFile; }

    public void setReadTimePerFile(int readTimePerFile) { this.readTimePerFile = readTimePerFile; }
}
