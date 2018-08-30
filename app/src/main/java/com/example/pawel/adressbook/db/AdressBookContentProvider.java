package com.example.pawel.adressbook.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.pawel.adressbook.R;

public class AdressBookContentProvider extends ContentProvider {

    /* Egzemplarz klasy - umożliwia obiektowi ContentProvider dostępu do bazy danych */
    private AdressBookDatabaseHelper dbHelper;

    /* Pomocnik obiektu ContentProvider */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /* Stałe obiektu UriMatcher (Określenie operacji do wykonania na bazie)*/
    private static final int ONE_CONTACT = 1; // Wykonanie operacji dla 1 kontaktu
    private static final int CONTACTS = 2; // Wykonanie operacji dla całej tabeli kontaktów


    /* Konfiguracja obiektu UriMatcher */
    static {
        /* Adres Uri kontaktu o okreslonym identyfikatorze(#) */
        URI_MATCHER.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);

        /* Adres Uri dla całęj tabeli kontaktów */
        URI_MATCHER.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        /* Liczba usuniętych wierszy */
        int numberOfDeletedRows;

        /* Sprawdzenie czy wybrany został kontakt */
        switch (URI_MATCHER.match(uri))
        {
            case ONE_CONTACT:
                /* Identyfikator kontaktu który ma zostać usunięty */
                String id = uri.getLastPathSegment();

                /*  Usunięcie kontaktu */
                numberOfDeletedRows = dbHelper.getReadableDatabase().delete(DatabaseDescription.Contact.TABLE_NAME,
                        DatabaseDescription.Contact._ID + "=" +id,selectionArgs );
                break;
                default: throw  new UnsupportedOperationException(getContext().getString(R.string.invalid_delete_uri)+ uri);
        }

        /* Powiadom obiekty nasłuchujące jeżeli usunięto */
        if (numberOfDeletedRows != 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        /* Zwrócenie informacji o usunięciu */
        return numberOfDeletedRows;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /* Deklaracja Uri */
        Uri newContactUri = null;

        /* Sprawdzenie czy adres uri odwołuje się do tabeli */
        switch (URI_MATCHER.match(uri))
        {
            case CONTACTS:
                /* Wstawienie nowego kontaktu do tabeli  */
                long rowId = dbHelper.getReadableDatabase().insert(DatabaseDescription.Contact.TABLE_NAME, null, values);

                /* Tworzenie adresu Uri dla dodanego kontaktu */
                /* Jeżeli dodanie się powiodło */
                if (rowId >0 )
                {
                    newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);
                    /* Powiadomienie obiektów  nasłuchujących zmian w tabeli */
                    getContext().getContentResolver().notifyChange(uri, null);

                }
                /* Jeżeli dodanie się nie powiodło */
                else {
                    throw new SQLException(getContext().getString(R.string.insert_failed) + uri);
                }
                break;
                default: throw new UnsupportedOperationException(getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        /* Zwrócenie adresu Uri */
        return newContactUri;
    }

    @Override
    public boolean onCreate() {
        /* Utworzenie obiektu AdressBookDBHelprer */
        dbHelper = new AdressBookDatabaseHelper(getContext());

        /* Operacja utworzenia obiektu contentprovider zakonczona sukcesem */
        if (dbHelper!= null) return true;
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        /* Obiekt SQLiteQueryBuilder  do tworzenia zapytań SQL */
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        /* Wybór jednego lub wszystkich kontaktów */
        switch (URI_MATCHER.match(uri)){
            case ONE_CONTACT:
                queryBuilder.appendWhere(DatabaseDescription.Contact._ID + "=" + uri.getLastPathSegment());
                break;

            case CONTACTS:

                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        /* Wykonanie zapytania SQL i inicjalizacja obiektu */
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(), projection,selection, selectionArgs,
                null,null,sortOrder);

        /* Konfiguracja obiektu Cursor */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        /* Zwrócenie obiektu cursor */
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        /* Przyjmuje wartość 1 jesli aktualizacja zakonczona sukcesem, w przeciwnym razem 0 */
        int numberOfRowsUpdated;

        /* Sprawdzenie adres uri czy wybrany jest jeden kontakt */
        switch (URI_MATCHER.match(uri))
        {
            case ONE_CONTACT:
                /* Odczytanie identyfikatora konttaktu który ma zostać zaktualizowany */
                String id = uri.getLastPathSegment();

                /* Aktualizacja zawartości kontaktu */
                numberOfRowsUpdated = dbHelper.getReadableDatabase().update(DatabaseDescription.Contact.TABLE_NAME, values,
                        DatabaseDescription.Contact._ID + "=" + id, selectionArgs);
                break;
                default: throw new UnsupportedOperationException(getContext().getString(R.string.invalid_update_uri));
        }

        /* Jeżeli dokonano aktualizacji to powiadom obiekty nasłuchujące */
        if (numberOfRowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null );
        }

        /* Zwrócenie informacji o aktualizacji */
        return numberOfRowsUpdated;
    }
}
