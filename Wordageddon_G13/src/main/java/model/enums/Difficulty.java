package model.enums;

/**
 * Enum che rappresenta i livelli di difficoltà del gioco,
 * ciascuno con caratteristiche specifiche come numero massimo di testi,
 * domande e tempo di lettura per file.
 */
public enum Difficulty {
    /**
     * Livello facile: 1 testo, 4 domande, 5 secondi di tempo di lettura per file.
     */
    EASY(1, 4, 5),

    /**
     * Livello medio: 2 testi, 8 domande, 3 secondi di tempo di lettura per file.
     */
    MEDIUM(2, 8, 3),

    /**
     * Livello difficile: 3 testi, 12 domande, 2 secondi di tempo di lettura per file.
     */
    HARD(3, 12, 2);

    private int maxTexts;
    private int maxQuestions;
    private int readTimePerFile;

    /**
     * Costruttore per il livello di difficoltà.
     *
     * @param maxTexts Numero massimo di testi da leggere.
     * @param maxQuestions Numero massimo di domande.
     * @param readTimePerFile Tempo massimo di lettura per ogni file, in secondi.
     */
    Difficulty(int maxTexts, int maxQuestions, int readTimePerFile) {
        this.maxTexts = maxTexts;
        this.maxQuestions = maxQuestions;
        this.readTimePerFile = readTimePerFile;
    }

    /** Restituisce il numero massimo di testi per il livello di difficoltà. */
    public int getMaxTexts() { return maxTexts; }

    /** Imposta il numero massimo di testi per il livello di difficoltà. */
    public void setMaxTexts(int maxTexts) { this.maxTexts = maxTexts; }

    /** Restituisce il numero massimo di domande per il livello di difficoltà. */
    public int getMaxQuestions() { return maxQuestions; }

    /** Imposta il numero massimo di domande per il livello di difficoltà. */
    public void setMaxQuestions(int maxQuestions) { this.maxQuestions = maxQuestions; }

    /** Restituisce il tempo di lettura per file in secondi. */
    public int getReadTimePerFile() { return readTimePerFile; }

    /** Imposta il tempo di lettura per file in secondi. */
    public void setReadTimePerFile(int readTimePerFile) { this.readTimePerFile = readTimePerFile; }
}
