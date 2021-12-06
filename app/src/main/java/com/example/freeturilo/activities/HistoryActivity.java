package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class HistoryActivity extends AppCompatActivity {
    private StorageManager storage;
    private List<RouteParameters> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        storage = new StorageManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        storage.loadHistoryAsync(this::onHistoryReady, new ToastStorageHandler(this));
    }

    private void onHistoryReady(List<RouteParameters> loadedHistory) {
        history = loadedHistory;
        HistoryAdapter adapter = new HistoryAdapter(this, history);
        ListView historyListView = findViewById(R.id.history_list);
        historyListView.setAdapter(adapter);
        historyListView.setOnItemClickListener(this::recreateRoute);
    }

    private void recreateRoute(@NonNull AdapterView<?> adapterView, @NonNull View view,
                               int position, long id) {
        RouteParameters routeParameters = history.get(position);
        history.remove(position);
        history.add(0, routeParameters);
        storage.saveHistoryAsync(history, new ToastStorageHandler(this));
        Bundle bundle = new Bundle();
        bundle.putBinder(getString(R.string.route_parameters_intent_name), new ObjectWrapperForBinder(routeParameters));
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}