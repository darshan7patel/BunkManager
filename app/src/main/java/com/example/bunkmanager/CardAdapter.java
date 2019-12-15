package com.example.bunkmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private SQLiteDatabase mdatabase;
    private Context mcontext;
    private Cursor mcursor;
    final CardDBHelper  dbHelper;


    public CardAdapter(Context context, Cursor cursor, SQLiteDatabase mdatabase, CardDBHelper dbHelper)
    {
        mcontext = context;
        mcursor = cursor;
        this.mdatabase=mdatabase;
        this.dbHelper = dbHelper;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView nameText;
        public Button attend,bunk;


        public CardViewHolder(@NonNull final View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.subName);
            attend = itemView.findViewById(R.id.attend);
            bunk = itemView.findViewById(R.id.bunk);

            attend.setOnClickListener(this);
            bunk.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.attend :
                    int attendValue = 0;
                    int bunkValue = 0;
                    String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + " WHERE " + CardContract.CardEntry._ID + "=" + v.getTag();
                    Cursor  cursor = mdatabase.rawQuery(query,null);
                    if (cursor.moveToFirst()) {
                        while (cursor.isAfterLast() != true) {
                            attendValue =  cursor.getInt(cursor.getColumnIndex("COLUMN_ATTEND"));
                            bunkValue =  cursor.getInt(cursor.getColumnIndex("COLUMN_BUNK"));
                            Log.d("Attend: ","" +attendValue);
                        }
                    }
                    int value = attendValue;
                    int bkValue = bunkValue;
                    value = value + 1;
                    dbHelper.updateData((String) v.getTag(),value,bkValue);
                    attend.setText("Attend: " + value);
                    break;
            }
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.mycard, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (!mcursor.moveToPosition(position))
        {
            return;
        }

        String name = mcursor.getString(mcursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME));
        int attend = mcursor.getInt(mcursor.getColumnIndex(CardContract.CardEntry.COLUMN_ATTEND));
        int bunk = mcursor.getInt(mcursor.getColumnIndex(CardContract.CardEntry.COLUMN_BUNK));
        long id = mcursor.getLong(mcursor.getColumnIndex(CardContract.CardEntry._ID));

        holder.nameText.setText(name);
        holder.attend.setText("Attend : " + String.valueOf(attend));
        holder.bunk.setText("Bunk : " + String.valueOf(bunk));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mcursor.getCount();
    }

    public void swapCursor(Cursor newCursor)
    {
        if (mcursor != null)
        {
            mcursor.close();
        }
        mcursor = newCursor;
        if (newCursor != null)
        {
            notifyDataSetChanged();
        }
    }
}
