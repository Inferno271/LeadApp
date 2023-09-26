package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CallNotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "call_notes.db";
    private static final int DATABASE_VERSION = 1;

    // Конструктор
    public CallNotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу для хранения заметок
        db.execSQL("CREATE TABLE IF NOT EXISTS call_notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "call_id INTEGER," +
                "note_title TEXT," +       // Добавляем поле для заголовка заметки
                "note_text TEXT," +
                "note_datetime TEXT" +     // Добавляем поле для даты и времени заметки
                ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Обновление до версии 2
            db.execSQL("ALTER TABLE call_notes ADD COLUMN note_title TEXT");
            db.execSQL("ALTER TABLE call_notes ADD COLUMN note_datetime TEXT");
        }
        // Добавьте другие обновления, если необходимо, для будущих версий
    }
}
