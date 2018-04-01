package com.example.siamsot.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookObj>> {


    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    ListView bookListView;
    boolean isConnected;
    /**
     * URL for books data from the Google Books API
     */
    private String mUrlRequestGoogleBooks = "";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * Circle progress bar
     */
    private View circleProgressBar;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;
    /**
     * Search field
     */
    private SearchView mSearchViewField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaration and initialization ConnectivityManager for checking internet connection
        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        /**
         * At the beginning check the connection with internet and save result to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);

        bookListView = findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<BookObj>());

        bookListView.setAdapter(mAdapter);

        // Find a reference to the empty view
        mEmptyStateTextView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Circle progress
        circleProgressBar = findViewById(R.id.loading_spinner);

        // Search button
        Button mSearchButton = findViewById(R.id.search_button);

        // Search field
        mSearchViewField = findViewById(R.id.search_view_field);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint("Enter a book title");


        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Progress bar mapping
            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected) + ". Sorry dude, no internet - no data :(");


            circleProgressBar.setVisibility(GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        // Set an item click listener on the Search Button, which sends a request to
        // Google Books API based on value from Search View
        mSearchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                // Check connection status
                checkConnection(cm);

                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateQueryUrl(mSearchViewField.getQuery().toString());
                    restartLoader();
                    Log.i(LOG_TAG, "Search value: " + mSearchViewField.getQuery().toString());
                } else {
                    // Clear the adapter of previous book data
                    mAdapter.clear();
                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

            }

        });

    }

    private String updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/books/v1/volumes?q=").append(searchValue).append("&filter=paid-ebooks&maxResults=40");
        mUrlRequestGoogleBooks = sb.toString();
        return mUrlRequestGoogleBooks;
    }

    @Override
    public Loader<List<BookObj>> onCreateLoader(int i, Bundle bundle) {
        Log.i("There is no instance", ": Created new one loader at the beginning!");
        // Create a new loader for the given URL
        updateQueryUrl(mSearchViewField.getQuery().toString());
        return new BookLoading(this, mUrlRequestGoogleBooks);
    }

    @Override
    public void onLoadFinished(Loader<List<BookObj>> loader, List<BookObj> books) {

        // Progress bar mapping
        View circleProgressBar = findViewById(R.id.loading_spinner);
        circleProgressBar.setVisibility(GONE);
        mEmptyStateTextView.setText(R.string.no_books);
        Log.i(LOG_TAG, ": Books has been moved to adapter's data set");

        // Clear the adapter of previous book data
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<BookObj>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG, ": Loader reset");
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;

            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected));

        } else {
            isConnected = false;
        }
    }

}