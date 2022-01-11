package com.example.freeturilo.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.freeturilo.misc.HistoryAdapter;
import com.example.freeturilo.misc.ObjectWrapperForBinder;
import com.example.freeturilo.R;
import com.example.freeturilo.storage.StorageConnector;
import com.example.freeturilo.storage.StorageHandler;
import com.example.freeturilo.storage.ToastStorageHandler;
import com.example.freeturilo.core.RouteParameters;

import java.util.List;

/**
 * An activity displaying a history of created routes.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see RouteParameters
 */
public class HistoryActivity extends FreeturiloActivity {
    /**
     * Stores the storage used by this activity to retrieve route history.
     */
    private final StorageConnector storage = new StorageConnector(this);
    /**
     * Stores the listview displaying the route history.
     */
    private ListView historyListView;
    /**
     * Stores the route history in form of a list of route parameters.
     */
    private List<RouteParameters> history;

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity and initializes {@link #historyListView} to recreate route
     * when an item is clicked.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see #recreateRoute
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListView = findViewById(R.id.history_list);
        historyListView.setOnItemClickListener(this::recreateRoute);
    }

    /**
     * Called when this activity is resumed.
     * <p>
     * Clears {@link #historyListView} and starts an asynchronous retrieval of
     * route history from the {@link #storage}.
     * @see StorageConnector#loadHistoryAsync
     */
    @Override
    protected void onResume() {
        super.onResume();
        historyListView.setAdapter(null);
        storage.loadHistoryAsync(this::onHistoryReady, new ToastStorageHandler(this));
    }

    /**
     * Called when route history has been retrieved from {@link #storage}.
     * <p>
     * Sets {@link #history} and populates {@link #historyListView}.
     * @param loadedHistory         a list of route parameters retrieved from
     *                              storage
     */
    private void onHistoryReady(@NonNull List<RouteParameters> loadedHistory) {
        history = loadedHistory;
        historyListView.setAdapter(new HistoryAdapter(this, history));
    }

    /**
     * Deletes an item from route history. Called when the delete history item
     * button is clicked.
     * <p>
     * Removes the history item from {@link #historyListView} and from
     * {@link #history}. Saves the history to {@link #storage}.
     * @param routeParameters       the history item to be deleted
     * @see StorageConnector#saveHistoryAsync
     */
    public void deleteHistoryItem(RouteParameters routeParameters) {
        HistoryAdapter adapter = (HistoryAdapter) historyListView.getAdapter();
        adapter.remove(routeParameters);
        history.remove(routeParameters);
        storage.saveHistoryAsync(history, new ToastStorageHandler(this));
    }

    /**
     * Recreates route. Called when a {@link #historyListView} item is clicked.
     * <p>
     * Moves the selected history item to the top of history. Saves history and
     * starts the {@code RouteActivity} with the selected
     * {@code RouteParameters}.
     * @param adapterView       unused parameter, included for compatibility
     *                          with {@code AdapterView.OnItemClickListener}
     * @param view              unused parameter, included for compatibility
     *                          with {@code AdapterView.OnItemClickListener}
     * @param position          an integer equal to the position of the clicked
     *                          item in the data set of {@link #historyListView}
     * @param id                unused parameter, included for compatibility
     *                          with {@code AdapterView.OnItemClickListener}
     * @see StorageConnector#saveHistoryAsync
     */
    private void recreateRoute(@NonNull AdapterView<?> adapterView, @NonNull View view,
                               int position, long id) {
        RouteParameters routeParameters = history.get(position);
        history.remove(position);
        history.add(0, routeParameters);
        storage.saveHistoryAsync(history, new ToastStorageHandler(this));
        Intent intent = new Intent(this, RouteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBinder(RouteActivity.ROUTE_PARAMETERS_INTENT, new ObjectWrapperForBinder(routeParameters));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}