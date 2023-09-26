package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import models.CallNote;

public class CallNotesDataSource {
    private SQLiteDatabase database;
    private CallNotesDatabaseHelper dbHelper;

    public CallNotesDataSource(Context context) {
        dbHelper = new CallNotesDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Метод для вставки заметки о звонке
    public long insertCallNote(CallNote callNote) {
        ContentValues values = new ContentValues();
        values.put("call_id", callNote.getCallId());
        values.put("note_title", callNote.getNoteTitle());
        values.put("note_text", callNote.getNoteText());
        values.put("note_datetime", callNote.getNoteDateTime());

        return database.insert("call_notes", null, values);
    }


    // Метод для обновления заметки о звонке
    public int updateCallNote(long id, String noteTitle, String noteText, String noteDateTime) {
        ContentValues values = new ContentValues();
        values.put("note_title", noteTitle);
        values.put("note_text", noteText);
        values.put("note_datetime", noteDateTime);

        return database.update("call_notes", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Метод для удаления заметки о звонке
    public void deleteCallNote(long callNoteId) {
        database.delete("call_notes", "id = ?", new String[]{String.valueOf(callNoteId)});
    }

    // Метод для получения списка всех заметок о звонках
    public List<CallNote> getAllCallNotes() {
        List<CallNote> callNotes = new ArrayList<>();
        Cursor cursor = database.query("call_notes", null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int callIdIndex = cursor.getColumnIndex("call_id");
                int noteTitleIndex = cursor.getColumnIndex("note_title");
                int noteTextIndex = cursor.getColumnIndex("note_text");
                int noteDateTimeIndex = cursor.getColumnIndex("note_datetime");

                do {
                    long id = cursor.getLong(idIndex);
                    long callId = cursor.getLong(callIdIndex);
                    String noteTitle = cursor.getString(noteTitleIndex);
                    String noteText = cursor.getString(noteTextIndex);
                    String noteDateTime = cursor.getString(noteDateTimeIndex);

                    CallNote callNote = new CallNote(id, callId, noteTitle, noteText, noteDateTime);
                    callNotes.add(callNote);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return callNotes;
    }
}
