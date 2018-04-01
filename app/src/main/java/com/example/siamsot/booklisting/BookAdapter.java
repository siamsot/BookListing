package com.example.siamsot.booklisting;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class BookAdapter extends ArrayAdapter<BookObj> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Activity context, ArrayList<BookObj> Books) {
        super(context, 0, Books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.books_list_view, parent, false);
        }

        // Get the current position of Book
        final BookObj currentBook = getItem(position);
        Log.i(LOG_TAG, "Item position: " + position);

        // Find the TextView in the list_item.xml (mapping)
        TextView titleBookTextView = listItemView.findViewById(R.id.title);
        TextView authorBookTextView = listItemView.findViewById(R.id.author);

        // Set proper value in each fields
        assert currentBook != null;
        titleBookTextView.setText(currentBook.getTitle());
        authorBookTextView.setText(currentBook.getAuthor());
        Log.i(LOG_TAG, "ListView has been returned");
        return listItemView;

    }


}