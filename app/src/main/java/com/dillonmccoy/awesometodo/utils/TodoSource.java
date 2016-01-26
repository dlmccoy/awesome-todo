package com.dillonmccoy.awesometodo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import com.dillonmccoy.awesometodo.models.TodoItem.TodoColumns;
import com.dillonmccoy.awesometodo.models.TodoItem;

public class TodoSource {
    TodoDBHelper dbHelper;
    SQLiteDatabase database;

    String[] allColumns = {
            TodoColumns._ID,
            TodoColumns.COLUMN_NAME_TASK,
            TodoColumns.COLUMN_NAME_PRIORITY
    };

    public TodoSource(Context context) {
        dbHelper = new TodoDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<TodoItem> getAllTodos() {
        ArrayList<TodoItem> items = new ArrayList<>();

        Cursor cursor = database.query(
                TodoColumns.TABLE_NAME,
                allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TodoItem item  = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }

    public TodoItem addTodoItem(String newItem) {
        ContentValues values = new ContentValues();

        values.put(TodoColumns.COLUMN_NAME_TASK, newItem);
        values.put(TodoColumns.COLUMN_NAME_PRIORITY, TodoItem.DEFAULT_PRIORITY);

        long insertId = database.insert(TodoColumns.TABLE_NAME, null, values);

        Cursor cursor = database.query(TodoColumns.TABLE_NAME,
                allColumns, TodoColumns.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TodoItem item = cursorToItem(cursor);
        cursor.close();
        return item;
    }

    private TodoItem cursorToItem(Cursor c) {
        return new TodoItem(c.getInt(0), c.getString(1), c.getInt(2));

    }

    public void saveItem(TodoItem item) {
        ContentValues values = new ContentValues();
        values.put(TodoColumns.COLUMN_NAME_TASK, item.task);
        values.put(TodoColumns.COLUMN_NAME_PRIORITY, item.priority);
        String[] selectionArgs = { String.valueOf(item.id) };
        database.update(TodoColumns.TABLE_NAME, values, TodoColumns.COLUMN_ID + " = ?", selectionArgs);
    }

    public void deleteItem(TodoItem item) {
        String[] deleteArgs = { String.valueOf(item.id) };
        database.delete(TodoColumns.TABLE_NAME, TodoColumns.COLUMN_ID + " = ?", deleteArgs);
    }
}
