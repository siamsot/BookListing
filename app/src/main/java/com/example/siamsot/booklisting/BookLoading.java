package com.example.siamsot.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


public class BookLoading extends AsyncTaskLoader<List<BookObj>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BookLoading.class.getName();

    private String mUrl;

    BookLoading(Context context, String url) {
        super(context);
        mUrl = url;

        Log.i(LOG_TAG, ": Loaded!");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("On start loading", ": Force loaded!");
    }

    @Override
    public List<BookObj> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<BookObj> books = Utilization.fetchBookData(mUrl);
        Log.i(LOG_TAG, ": Loaded in background!");
        return books;

    }
}
