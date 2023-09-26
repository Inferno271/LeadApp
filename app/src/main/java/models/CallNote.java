package models;

public class CallNote {
    private long id; // Идентификатор заметки
    private long callId; // Идентификатор звонка, к которому относится заметка
    private String noteTitle;
    private String noteText;
    private String noteDateTime;

    // Конструктор для создания новой заметки
    public CallNote(long callId, String noteTitle, String noteText, String noteDateTime) {
        this.callId = callId;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.noteDateTime = noteDateTime;
    }

    // Конструктор для загрузки существующей заметки из базы данных
    public CallNote(long id, long callId, String noteTitle, String noteText, String noteDateTime) {
        this.id = id;
        this.callId = callId;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.noteDateTime = noteDateTime;
    }

    // Геттеры и сеттеры для полей
    public long getId() {
        return id;
    }

    public long getCallId() {
        return callId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteDateTime() {
        return noteDateTime;
    }

    public void setNoteDateTime(String noteDateTime) {
        this.noteDateTime = noteDateTime;
    }
}
