package com.example.svenu.svenuitendaalpset4;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by svenu on 23-11-2017.
 */

public abstract class TodoAdapter extends ResourceCursorAdapter {
    public TodoAdapter(Context context, Cursor cursor){
        super(context, R.layout.row_todo, cursor);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.todoItem);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        String title = cursor.getString(cursor.getColumnIndex(TodoDatabase.COL2));
        boolean completed = cursor.getInt(cursor.getColumnIndex(TodoDatabase.COL3)) > 0;
        int textColor = cursor.getInt(cursor.getColumnIndex(TodoDatabase.COL4));

        textView.setText(title);
        checkBox.setChecked(completed);
        checkBox.setButtonTintList(ColorStateList.valueOf(textColor));

        if (completed) {
            textView.setTextColor(textColor);
        }
        else {
            textView.setTextColor(Color.BLACK);

        }
    }
}
