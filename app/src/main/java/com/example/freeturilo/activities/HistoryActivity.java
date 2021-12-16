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
import com.example.freeturilo.storage.StorageManager;
import com.example.freeturilo.storage.ToastStorageHandler;
import com.example.freeturilo.core.RouteParameters;

import java.util.List;

public class HistoryActivity extends FreeturiloActivity {
    private final StorageManager storage = new StorageManager(this);
    private ListView historyListView;
    private List<RouteParameters> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListView = findViewById(R.id.history_list);
        historyListView.setOnItemClickListener(this::recreateRoute);
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyListView.setAdapter(null);
        storage.loadHistoryAsync(this::onHistoryReady, new ToastStorageHandler(this));
    }

    private void onHistoryReady(@NonNull List<RouteParameters> loadedHistory) {
        history = loadedHistory;
        historyListView.setAdapter(new HistoryAdapter(this, history));
    }

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