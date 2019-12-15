package com.example.bunkmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mdatabase;
    private CardAdapter mAdapter;
    private FloatingActionButton btnadd;
    private TextView option;
    Button buttonattend, buttonbunk;
    private int mAttend = 0, mBunk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CardDBHelper  dbHelper = new CardDBHelper(this);
        mdatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CardAdapter(this, getAllItemes(),mdatabase,dbHelper);
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        View inflatedView = getLayoutInflater().inflate(R.layout.mycard, null);

        btnadd = findViewById(R.id.fab_add);
        option = inflatedView.findViewById(R.id.txtOptionDigit);
        buttonattend = inflatedView.findViewById(R.id.attend);
        buttonbunk = inflatedView.findViewById(R.id.bunk);
//
//        buttonattend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Cursor cursor = mdatabase.execSQL("SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + " WHERE " + CardContract.CardEntry._ID + "=" + v.getTag());
//                Cursor cursor = dbHelper.getData((String)v.getTag());
//                int attendValue = cursor.getInt(1);
//                mAttend = attendValue + 1;
//                Log.d("Attend", "" +  mAttend);
//                dbHelper.updateData((String)v.getTag(),mAttend,mBunk);
//                mAdapter.swapCursor(getAllItemes());
//            }
//        });

//        buttonbunk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increseBunk();
//            }
//        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Enter Subject");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        String sub = input.getText().toString().trim();
                        int attend = 0, bunk = 0;
                        if (TextUtils.isEmpty(sub)){
                            Toasty.warning(MainActivity.this,"Please Enter Subject",Toasty.LENGTH_SHORT).show();
                        }
                        else {
                                addItem(sub);
                        }
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_item_save:
                                Toasty.success(MainActivity.this,"Saved",Toasty.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_item_delete:
                                //Delete Item

                                Toasty.error(MainActivity.this, "Swipe to Delete", Toasty.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void addItem(String sub)
    {
        ContentValues cv = new ContentValues();
        cv.put(CardContract.CardEntry.COLUMN_NAME, sub);
        cv.put(CardContract.CardEntry.COLUMN_ATTEND, mAttend);
        cv.put(CardContract.CardEntry.COLUMN_BUNK, mBunk);

        mdatabase.insert(CardContract.CardEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItemes());
    }

    private void removeItem(long id)
    {
        mdatabase.delete(CardContract.CardEntry.TABLE_NAME,
                CardContract.CardEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItemes());
    }

    private Cursor getAllItemes()
    {
        return mdatabase.query(
                CardContract.CardEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CardContract.CardEntry._ID
        );
    }
}
