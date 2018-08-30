package com.example.pawel.adressbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdressBookDatabaseHelper extends SQLiteOpenHelper {

    /* Nazwa tworzonej bazy danych */
    private static final String DB_NAME = "AdressBook.db";

    /* Wersja bazy danych */
    private static final int DB_VERSION= 1;


    /* Konstruktor tworzący bazę danych */
    public AdressBookDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        /* Zapytanie SQL tworzące tabelę w bazie danych */
        final String CREATE_CONTACTS_TABLE = "CREATE TABLE" + DatabaseDescription.Contact.TABLE_NAME+
                "(" + DatabaseDescription.Contact._ID + " integer primary key, " +
                DatabaseDescription.Contact.COLUMN_NAME + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_PHONE + " TEXT, "+
                DatabaseDescription.Contact.COLUMN_EMAIL + " TEXT, "+
                DatabaseDescription.Contact.COLUMN_STREET + " TEXT, "+
                DatabaseDescription.Contact.COLUMN_CITY + " TEXT, "+
                DatabaseDescription.Contact.COLUMN_STATE + " TEXT, "+
                DatabaseDescription.Contact.COLUMN_ZIP + " TEXT);";

        /* Wykonanie zapytania  */
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
