package com.dillonmccoy.awesometodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoArrayAdapter extends ArrayAdapter<TodoItem> {

    public TodoArrayAdapter(Context context, ArrayList<TodoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView taskText = (TextView) convertView.findViewById(R.id.taskText);

        // Populate the data into the template view using the data object
        taskText.setText(item.task);

        // Return the completed view to render on screen
        return convertView;
    }
}
