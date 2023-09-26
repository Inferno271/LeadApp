package com.inferno271.leadapp15;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import models.Contact;
import com.inferno271.leadapp15.R;


public class ContactDetailFragment extends Fragment {

    private TextView nameTextView;
    private TextView phoneNumberTextView;

    public ContactDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        // Инициализация TextView для отображения имени и номера телефона контакта
        nameTextView = root.findViewById(R.id.textViewContactName);
        phoneNumberTextView = root.findViewById(R.id.textViewContactPhoneNumber);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Получение выбранного контакта из аргументов фрагмента
        Bundle arguments = getArguments();
        if (arguments != null) {
            Contact selectedContact = arguments.getParcelable("selectedContact");

            if (selectedContact != null) {
                // Отображение имени и номера телефона контакта
                nameTextView.setText(selectedContact.getName());
                phoneNumberTextView.setText(selectedContact.getPhoneNumber());
            }
        }
    }
}
