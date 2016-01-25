package com.dillonmccoy.awesometodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dillonmccoy.awesometodo.TodoItemContract.TodoEntry;

public class TodoSource {
    TodoDBHelper dbHelper;
    SQLiteDatabase database;

    String[] allColumns = {
            TodoItemContract.TodoEntry._ID,
            TodoItemContract.TodoEntry.COLUMN_NAME_TASK,
            TodoItemContract.TodoEntry.COLUMN_NAME_PRIORITY
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
                TodoItemContract.TodoEntry.TABLE_NAME,
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

        values.put(TodoEntry.COLUMN_NAME_TASK, newItem);
        values.put(TodoEntry.COLUMN_NAME_PRIORITY, 3);

        long insertId = database.insert(TodoEntry.TABLE_NAME, null, values);

        Cursor cursor = database.query(TodoEntry.TABLE_NAME,
                allColumns, TodoEntry.COLUMN_ID + " = " + insertId, null,
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
        values.put(TodoEntry.COLUMN_NAME_TASK, item.task);
        values.put(TodoEntry.COLUMN_NAME_PRIORITY, item.priority);
        String[] selectionArgs = { String.valueOf(item.id) };
        database.update(TodoEntry.TABLE_NAME, values, TodoEntry.COLUMN_ID + " = ?", selectionArgs);
    }

    public void deleteItem(TodoItem item) {
        String[] deleteArgs = { String.valueOf(item.id) };
        database.delete(TodoEntry.TABLE_NAME, TodoEntry.COLUMN_ID + " = ?", deleteArgs);
    }
}
