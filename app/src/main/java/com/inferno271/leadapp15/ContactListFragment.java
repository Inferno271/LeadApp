package com.inferno271.leadapp15;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.List;

import data.ContactsDataSource;
import models.Contact;

public class ContactListFragment extends Fragment {

    private List<Contact> contacts;
    private ContactsDataSource dataSource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new ContactsDataSource(requireContext());
        dataSource.open();
        contacts = dataSource.getAllContacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ListView listViewContacts = rootView.findViewById(R.id.listViewContacts);

        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, contacts);
        listViewContacts.setAdapter(adapter);

        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            // Обработка нажатия на контакт - открытие подробных сведений и заметок
            // В методе onItemClick фрагмента ContactListFragment
            Contact selectedContact = contacts.get(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable("selectedContact", selectedContact);
            Navigation.findNavController(view).navigate(R.id.action_contactListFragment_to_contactDetailFragment, bundle);
            // Здесь можно запустить фрагмент для отображения подробных сведений о контакте и связанных с ним заметок
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
