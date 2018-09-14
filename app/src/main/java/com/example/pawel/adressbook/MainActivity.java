package com.example.pawel.adressbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ContactsFragment.ContactsFragmentListner,
        DetailFragment.DetailFragmentListner, AddEditFragment.AddEditFragmentListner {

    /* Klucz przechowujący adres Uri kontaktu w obiekcie przekazanym do fragmentu  */
    public static final String CONTACT_URI="contact_uri";

    /* Fragment wyświetlający listę kontaktów z bazy */
    private ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Jeżeli rozkłąd głownej aktywności zawiera fragment FragmentContainer(content_main.xml */
        /* to oznacza używanie rozkładu dla telefonu. Tworzenei i wyświetlenie fragmentu contactsFragment*/
        if (savedInstanceState == null && findViewById(R.id.fragmentContainer )!= null)
        {
            /* Utworzenei fragmenu ContactsFragment */
            contactsFragment = new ContactsFragment();

            /* Dodanie fragmentu do rozkładu FrameLayout */
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment);

            /* Wyświetl obiekt ContactsFragment */
            transaction.commit();
        }
        else {
            /* Uzyskanie odwołania do już istniejącego fragmentu ContactsFragment */
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);
        }

    }

    /* Utworzenie i wyświetlenie fragmentu DetailFragment */
    private void displayDetailFragment(Uri contactUri, int viewID){
        /* Utworzenie fragmentu DetailFragment */
        DetailFragment detailFragment = new DetailFragment();

        /* Przekazanie adresu Uri jako fragmentu */
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri );
        detailFragment.setArguments(arguments);


        /* Dodanie fragmentu do rozkładu */
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(viewID, detailFragment);
        fragmentTransaction.addToBackStack(null);

        /* Wyświetl obiekt DetailFragment */
        fragmentTransaction.commit();

    }

    /* Utworzenie i wyświetlenie fragmentu addEditFragment */
    private void displayAddEditFragment(Uri contactUri, int viewID)
    {
    /* Utworzenie fragmentu addEditFragment */
    AddEditFragment addEditFragment = new AddEditFragment();

     /* Jeżeli edytowany jset wcześniej zapisany kontakt to jako argument przekazywany jest contactUri */
     if (contactUri != null)
     {
         Bundle arguments = new Bundle();
         arguments.putParcelable(CONTACT_URI, contactUri);
         addEditFragment.setArguments(arguments);
     }

     /* Dodanie fragmentu do rozkladu */
     FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
     transaction.replace(viewID, addEditFragment);

     transaction.addToBackStack(null);

     /* Wyświetl obiekt addEditFragment */
     transaction.commit();

    }

    @Override
    public void onAddEditCompleted(Uri contactUri) {
        /* Usunięcie  górnego elementu stosu aplikacji */
        getSupportFragmentManager().popBackStack();

        /* Odświeżenie listy kontaktów */
        contactsFragment.updateContactList();

        /* Obsługa  aktualizacji na tablecie */
        if (findViewById(R.id.fragmentContainer) == null)
        {
            /* Usunięcie górnego elementu stosu aplikacji */
            getSupportFragmentManager().popBackStack();

            /* Wyświetlenie kontaktu który został dodany lub zaktualizowany */
            displayDetailFragment(contactUri, R.id.rightPaneContainer);
        }

    }

    @Override
    public void onContactSelected(Uri contactUri) {

        /* Wyświetlenie fragmentu DetailFragment dla wybranego kontaktu na telefonie */
        if (findViewById(R.id.fragmentContainer ) != null)
        {
            displayDetailFragment(contactUri, R.id.fragmentContainer);
        }
        /* Jeżeli uruchamiamy aplikację na tablecie */
        else {
            getSupportFragmentManager().popBackStack();
            displayDetailFragment(contactUri, R.id.rightPaneContainer);
        }
    }

    @Override
    public void onAddContact() {
        /* Wyświetlenie fragmentu AddEditFragment dla wybranego kontaktu na telefonie */
        if (findViewById(R.id.fragmentContainer) != null)
        {
            displayAddEditFragment(null, R.id.fragmentContainer);
        }

        /* ... na tablecie */
        else {
            displayAddEditFragment(null, R.id.rightPaneContainer);
        }
    }

    @Override
    public void onContactDeleted() {
        /* Usunięcie elementu znajdującego się na szczycie stosu aplikacji */
        getSupportFragmentManager().popBackStack();

        /* Odświeżenie listy kontaktów fragmentu ContactsFragment */
        contactsFragment.updateContactList();

    }

    @Override
    public void onEditContact(Uri contactUri) {
        /* Wyświetlenie fragmentu AddEditFragment dla wybranego kontaktu na telefonie */
        if (findViewById(R.id.fragmentContainer) != null)
        {
            displayAddEditFragment(contactUri, R.id.fragmentContainer);
        }

        /* ... na tablecie */
        else {
            displayAddEditFragment(contactUri, R.id.rightPaneContainer);
        }

    }
}
