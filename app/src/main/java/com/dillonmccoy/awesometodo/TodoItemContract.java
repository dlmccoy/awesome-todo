package com.dillonmccoy.awesometodo;

import android.provider.BaseColumns;

public final class TodoItemContract {
    public TodoItemContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
}
