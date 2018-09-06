package com.example.pawel.adressbook;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pawel.adressbook.db.DatabaseDescription;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    /* Definicja Interffejsu implementowanego przez klasę ContactsFragment */
    public interface ContactClickListner{
        void onClick(Uri ContactUriUri);
    }


    /* Klasa używana do implementacji wzorca ViewHolder w kontekście widoku RecyclerView */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /* TextView wyświetlający nazwe kontaktu */
        public final TextView textView;
        /* Identyfikator rzędu kontaktu */
        private long rowId;

        /* Konstruktor klasy ViewHolder */
        public ViewHolder(View itemView) {
            super(itemView);
            /* Inicjalizacja widoku TextView */
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            /* Podłącz do obiektu view obiekt nasłuchujący zdarzeń */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListner.onClick(DatabaseDescription.Contact.buildContactUri(rowId));
                }
            });
        }

        /* Określenie identyfikatora rzędu */
        public void setRowId(long rowId)
        {
        this.rowId = rowId;
        }
    }

    /* Zmienne egzemplarzowe */
    private Cursor cursor = null;
    private final ContactClickListner clickListner;

    /* Konstruktor klasy Contaccts Adapter */
    public ContactsAdapter(ContactClickListner clickListner)
    {
        this.clickListner = clickListner;
    }

    /* Uzyskanie obiektu ViewHolder bieżącego elementu kontaktu */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Przygotowanie do wyświetlenia predefiniowanego rozkładu Androida */
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,false);

        /* Zwrócenie obiektu ViewHolder bieżacego elementu */
        return new ViewHolder(view);
    }

    /* Określenie tekstu elementu listy */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /* Przeniesienie wybranego kontaktu w odpowiednie miejsce widoku RecyclerView */
        cursor.moveToPosition(position);

        /* Określenie identyfikatora rowId elementu ViewHolder */
        holder.setRowId(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));

        /* Ustawienie tekstu widoku TextView elementu widoku RecyclerView */
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME)));
    }




    @Override
    public int getItemCount() {
        return (cursor != null)? cursor.getCount() :0;
    }
    /* Zamienia bieżacy obiekt cursor na nowy */
    public void swapCursor(Cursor cursor)
    {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
