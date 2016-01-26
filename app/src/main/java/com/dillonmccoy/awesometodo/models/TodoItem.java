package com.dillonmccoy.awesometodo.models;

import android.provider.BaseColumns;

public class TodoItem {
    public String task;
    public int priority;
    public int id;
    public static int DEFAULT_PRIORITY = 2;

    public TodoItem(int id, String task) {
        this(id, task, DEFAULT_PRIORITY);
    }
    public TodoItem(int id, String task, int priority) {
        this.id = id;
        this.task = task;
        this.priority = priority;
    }

    /* Inner class that defines the table contents */
    public static abstract class TodoColumns implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
}
