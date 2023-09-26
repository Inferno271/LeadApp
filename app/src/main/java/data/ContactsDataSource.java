package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import models.Contact;

public class ContactsDataSource {
    private SQLiteDatabase database;
    private ContactsDatabaseHelper dbHelper;

    public ContactsDataSource(Context context) {
        dbHelper = new ContactsDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Метод для вставки контакта
    public long insertContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone_number", contact.getPhoneNumber());
        // Здесь можно добавить другие поля контакта, если они есть

        return database.insert("contacts", null, values);
    }

    // Метод для обновления контакта
    public int updateContact(long id, String name, String phoneNumber) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone_number", phoneNumber);
        // Здесь можно добавить другие поля контакта, если они есть

        return database.update("contacts", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Метод для удаления контакта
    public void deleteContact(long contactId) {
        database.delete("contacts", "id = ?", new String[]{String.valueOf(contactId)});
    }

    // Метод для получения списка всех контактов
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = database.query("contacts", null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int phoneNumberIndex = cursor.getColumnIndex("phone_number");
                // Здесь добавьте индексы других полей контакта, если они есть

                do {
                    long id = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex);
                    // Здесь считайте остальные поля контакта, если они есть

                    Contact contact = new Contact(id, name, phoneNumber);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return contacts;
    }
}
