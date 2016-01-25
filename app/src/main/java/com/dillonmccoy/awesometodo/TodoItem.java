package com.dillonmccoy.awesometodo;

public class TodoItem {
    public String task;
    public int priority;
    public int id;

    public TodoItem(int id, String task, int priority) {
        this.id = id;
        this.task = task;
        this.priority = priority;
    }
}
