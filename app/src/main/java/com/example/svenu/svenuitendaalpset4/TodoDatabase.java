package com.example.svenu.svenuitendaalpset4;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by svenu on 20-11-2017.
 */

public class TodoDatabase extends SQLiteOpenHelper {
    private static TodoDatabase instance;

    private static final String TABLE_NAME = "todos";
    public static final String COL1 = "_id";
    public static final String COL2 = "title";
    public static final String COL3 = "completed";
    public static final String COL4 = "colour";
    private Context context;

    private TodoDatabase(Context aContext) {
        super(aContext , TABLE_NAME, null, 1);
        context = aContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT NOT NULL UNIQUE, " + COL3 + " BOOLEAN, " + COL4 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static TodoDatabase getInstance(Context context) {
        if(instance == null) {
            // call the private constructor
            instance = new TodoDatabase(context);
        }
        return instance;
    }

    public boolean insert(String itemTitle, int textColor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, itemTitle);
        contentValues.put(COL3, 0);
        contentValues.put(COL4, textColor);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void update(long id, int textColor) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 + " = (CASE " + COL3 + " WHEN 1 THEN 0 ELSE 1 END) WHERE " + COL1 + " = " + id + ";";
        String query2 = "UPDATE " + TABLE_NAME + " SET " + COL4 + " = " + textColor + " WHERE " + COL1 + " = " + id + ";";
        db.execSQL(query);
        db.execSQL(query2);
    }

    public void update(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 + " = (CASE " + COL3 + " WHEN 1 THEN 0 ELSE 1 END) WHERE " + COL1 + " = " + id + ";";
        db.execSQL(query);
    }


    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = " + id + ";";
        db.execSQL(query);
    }

    public void deleteSelected() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL3 + " = 1;";
        db.execSQL(query);
    }

    public void copySelected() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME + " WHERE " + COL3 + " = 1;";
        Cursor data = db.rawQuery(query, null);
        String copyText = "";
        while (data.moveToNext()) {
            copyText = copyText + data.getString(data.getColumnIndex(TodoDatabase.COL2)) + "\n";
        }
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", copyText);

        try {
            myClipboard.setPrimaryClip(myClip);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
