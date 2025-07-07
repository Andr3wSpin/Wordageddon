package model.enums;

public enum QuestionType {

    TYPE1("Quante volte compare la parola '<parola>' nel documento '<nome_documento>'?:"),
    TYPE2("Quale tra le seguenti parole è la più frequente in tutti i documenti?:"),
    TYPE3("Quale di queste parole è presente in tutti i documenti?:"),
    TYPE4("Quale tra le seguenti parole è più frequente nel documento '<nome_documento>'?:");

    private String text;

    QuestionType(String text) {
        this.text = text;
    }

    public String getText() { return text; }
}
