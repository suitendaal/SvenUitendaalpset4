package com.example.svenu.svenuitendaalpset4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button addButton;
    private EditText editText;
    private ListView theListView;
    private SeekBar seekBar;
    private TodoAdapter theAdapter;
    private TodoDatabase db;
    private int textColor = Color.RED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.editText);
        theListView = findViewById(R.id.theListView);
        seekBar = findViewById(R.id.seekBar);

        setColors();

        db = TodoDatabase.getInstance(MainActivity.this);
        theAdapter = new TodoAdapter(this, db.selectAll()) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
            }
        };

        theListView.setAdapter(theAdapter);
        theListView.setOnItemClickListener(new GoItemClickListener());
        theListView.setOnItemLongClickListener(new GoItemLongClickListener());
        addButton.setOnClickListener(new GoButtonClickListener());
        seekBar.setOnSeekBarChangeListener(new GoSeekbarChangeListener());
    }

    private void setColors() {
        editText.setBackgroundTintList(ColorStateList.valueOf(textColor));
        addButton.setTextColor(ColorStateList.valueOf(textColor));
        addButton.setBackgroundTintList(ColorStateList.valueOf(textColor));
        seekBar.setProgressTintList(ColorStateList.valueOf(textColor));
        seekBar.setThumbTintList(ColorStateList.valueOf(textColor));
//        getWindow().setStatusBarColor(textColor);
//        getWindow().setNavigationBarColor(textColor);
    }

    private void updateData() {
        Cursor cursor = db.selectAll();
        theAdapter.swapCursor(cursor);
    }

    public void apology(String apologyWord) {
        Toast.makeText(this, apologyWord, Toast.LENGTH_SHORT).show();
    }

    public void buttonClicked(View button) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        OptionFragment fragment = new OptionFragment(theAdapter);
        fragment.show(ft, "dialog");
    }

    private class GoButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TodoDatabase db = TodoDatabase.getInstance(MainActivity.this);
            String newEntry = editText.getText().toString();
            if (editText.length() != 0) {
                boolean inserted = db.insert(newEntry, textColor);
                if (inserted){
                    apology(newEntry + " added!");
                    updateData();
                }
                else {
                    apology("Something went wrong");
                }
                editText.setText("");
            }
            else {
                apology("No text filled in");
            }
        }
    }

    private class GoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            if (checkBox.isChecked()){
                db.update(id);
            }
            else {
                db.update(id, textColor);
            }
            updateData();
        }
    }

    private class GoItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, final long id) {
            TextView itemTitleView = view.findViewById(R.id.todoItem);
            String itemTitle = itemTitleView.getText().toString();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("Are you sure you want to delete " + itemTitle + "?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            db.delete(id);
                            updateData();
                            apology("Item deleted");
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }
    }

    private class GoSeekbarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            int rgbRed;
            int rgbGreen;
            int rgbBlue;
            if (progress < 100 / 6) {
                rgbRed = 255;
                rgbGreen = 255 * 6 / 100 * progress;
                rgbBlue = 0;
            }
            else if (progress < 100 / 3) {
                rgbRed = 510 - 255 * 6 / 100 * progress;
                rgbGreen = 255;
                rgbBlue = 0;
            }
            else if (progress < 100 / 2) {
                rgbRed = 0;
                rgbGreen = 255;
                rgbBlue = -510 + 255 * 6 / 100 * progress;
            }
            else if (progress < 100 * 2 / 3) {
                rgbRed = 0;
                rgbGreen = 1020 - 255 * 6 / 100 * progress;
                rgbBlue = 255;
            }
            else if (progress < 100 * 5 / 6) {
                rgbRed = - 1020 + 255 * 6 / 100 * progress;
                rgbGreen = 0;
                rgbBlue = 255;
            }
            else {
                rgbRed = 255;
                rgbGreen = 0;
                rgbBlue = 1530 - 255 * 6 / 100 * progress;
            }

            if (rgbRed > 255) {
                rgbRed = 255;
            }
            else if (rgbRed < 0) {
                rgbRed = 0;
            }

            if (rgbGreen > 255) {
                rgbGreen = 255;
            }
            else if (rgbGreen < 0) {
                rgbGreen = 0;
            }
            if (rgbBlue > 255) {
                rgbBlue = 255;
            }
            else if (rgbBlue < 0) {
                rgbBlue = 0;
            }

            textColor = Color.rgb(rgbRed, rgbGreen, rgbBlue);
            setColors();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
