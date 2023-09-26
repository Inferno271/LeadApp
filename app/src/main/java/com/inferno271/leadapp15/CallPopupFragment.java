package com.inferno271.leadapp15;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import data.CallNotesDataSource;
import models.Call;
import models.CallNote;

public class CallPopupFragment extends Fragment {

    private Call call;
    private EditText editTextNoteTitle;
    private EditText editTextNoteText;

    public CallPopupFragment() {
        // Обязательный пустой конструктор
    }

    // Создание нового экземпляра фрагмента с передачей объекта Call
    public static CallPopupFragment newInstance(Call call) {
        CallPopupFragment fragment = new CallPopupFragment();
        Bundle args = new Bundle();
        args.putParcelable("call", call);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получение объекта Call из переданных аргументов
        if (getArguments() != null) {
            call = getArguments().getParcelable("call");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Создание и настройка интерфейса фрагмента
        View view = inflater.inflate(R.layout.fragment_call_popup, container, false);

        // Находим элементы интерфейса
        TextView textViewCallerName = view.findViewById(R.id.textViewCallerName);
        TextView textViewPhoneNumber = view.findViewById(R.id.textViewPhoneNumber);
        TextView textViewLastCallDate = view.findViewById(R.id.textViewLastCallDate);
        editTextNoteTitle = view.findViewById(R.id.editTextNoteTitle);
        editTextNoteText = view.findViewById(R.id.editTextNote);
        Button buttonSaveNote = view.findViewById(R.id.buttonSaveNote);

        // Настраиваем отображение информации о звонке
        if (call != null) {
            textViewCallerName.setText(call.getCallerName());
            textViewPhoneNumber.setText(call.getPhoneNumber());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            Date date = new Date(Long.parseLong(call.getCallDateTime()));
            textViewLastCallDate.setText(dateFormat.format(date));
        }

        // Обработчик нажатия на кнопку "Сохранить"
        buttonSaveNote.setOnClickListener(v -> saveNote());

        return view;
    }

    // Метод для сохранения заметки
    private void saveNote() {
        // Получаем текст из полей заметки
        String noteTitle = editTextNoteTitle.getText().toString().trim();
        String noteText = editTextNoteText.getText().toString().trim();

        // Проверяем, что хотя бы одно поле не пустое
        if (!noteTitle.isEmpty() || !noteText.isEmpty()) {
            CallNotesDataSource dataSource = new CallNotesDataSource(requireContext());
            dataSource.open();

            // Создаем объект CallNote с переданными данными
            CallNote callNote = new CallNote(call.getId(), noteTitle, noteText, getCurrentDateTime());

            // Передаем объект CallNote методу insertCallNote
            dataSource.insertCallNote(callNote);

            dataSource.close();
            clearNoteFields();

            // Возвращаемся назад при сохранении заметки
            requireActivity().finish();
        }
    }

    // Метод для очистки полей ввода заметки
    private void clearNoteFields() {
        editTextNoteTitle.setText("");
        editTextNoteText.setText("");
    }

    // Метод для получения текущей даты и времени
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
