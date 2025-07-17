package model.enums;

/**
 * Enum che rappresenta i vari tipi di domande possibili nel gioco,
 * ciascuno con il relativo testo della domanda, contenente placeholder
 * per parole o nomi di documenti da sostituire dinamicamente.
 */
public enum QuestionType {

    /**
     * Tipo 1: Chiede quante volte una parola specifica compare in un documento specifico.
     * Placeholder: parola, nome_documento
     */
    TYPE1("Quante volte compare la parola '<parola>' nel documento '<nome_documento>'?:"),

    /**
     * Tipo 2: Chiede quale tra le parole proposte è la più frequente in un documento specifico.
     * Placeholder: nome_documento
     */
    TYPE2("Quale tra le seguenti parole è più frequente nel documento '<nome_documento>'?:"),

    /**
     * Tipo 3: Chiede quale parola è presente in tutti i documenti dati.
     */
    TYPE3("Quale di queste parole è presente in tutti i documenti?:"),

    /**
     * Tipo 4: Chiede quale tra le parole proposte è la più frequente in tutti i documenti.
     */
    TYPE4("Quale tra le seguenti parole è la più frequente in tutti i documenti?:");

    private String text;

    /**
     * Costruttore per il tipo di domanda.
     *
     * @param text Testo della domanda, con eventuali placeholder.
     */
    QuestionType(String text) {
        this.text = text;
    }

    /**
     * Restituisce il testo associato al tipo di domanda.
     *
     * @return testo della domanda con placeholder.
     */
    public String getText() { return text; }
}
