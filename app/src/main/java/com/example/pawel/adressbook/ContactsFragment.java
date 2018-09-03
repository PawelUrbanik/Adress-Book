package com.example.pawel.adressbook;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.adressbook.db.DatabaseDescription;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /* Metody wywołania zwrotnego implementowane przez klasę MainActivity */
    public interface ContactsFragmentListner {

        /* Wywyołania w wyniku wybrania kontaktu */
        void onContactSelected(Uri uri);

        /* Wywyołania w wyniku dotknięcia przycisku dodawania */
        void onAddContact();
    }

    /* Identyfikator obiektu loader */
    public static final int CONTACTS_LOADER = 0;

    /* Obiekt informujący aktywność MainActivity o wybrraniu kontaktu */
    private ContactsFragmentListner listner;

    /* Adapter obiektu RecyclerView */
    private ContactsAdapter contactsAdapter;


    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setHasOptionsMenu(true);

        /* Przygotowanie do wyświetlenia GUI */
        View view = inflater.inflate(R.layout.fragment_contacts, container,false);

        /* Uzyskanie odwołania do widoku recyclerView */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        /* Konfiguracja widoku RecyclerView (pionowa lista) */
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        /*  */
        contactsAdapter = new ContactsAdapter(new ContactsAdapter().ContactClickListner(){

            @Override
            public void onClick(Uri contactUri)
            {
                listner.onContactSelected(contactUri);
            }
        });

        /* Ustawienie adaptera widoku RecyclerView*/
        recyclerView.setAdapter(contactsAdapter);

        /* Dołączenie spersonalizowanego obiektu ItemDivider */
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        /* Rozmiar widoku Recyclerview nie ulega zmianie */
        recyclerView.setHasFixedSize(true);

        /* Inicjalizacja i konfiguracja przycisku dodawania kontaktu (+) */
        FloatingActionButton addButton = (FloatingActionButton)  view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onAddContact();
            }
        });

        /* Zwrócenie widoku GUI */
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        listner = (ContactsFragmentListner) context;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listner= null;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState)
    {
        super.onActivityCreated(saveInstanceState);
        getLoaderManager().initLoader(CONTACTS_LOADER, null,this);
    }

    public void updateContactList()
    {
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /* Utworzenie obiektu CursorLoader */
        switch (id){
            case CONTACTS_LOADER:
                return new CursorLoader(getActivity(), DatabaseDescription.Contact.CONTENT_URI, //AdresUriTablei kontaktw
                        null, //null zwraca wszystkie kolumny
                        null, //zwraca wszystkie wiersze
                        null, //Brak argumentów selekcji
                        DatabaseDescription.Contact.COLUMN_NAME + "COLLATE NOCASE ASC"  );// Kolejność sortowania
            default: return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactsAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactsAdapter.swapCursor(null);
    }
}
