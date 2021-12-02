package com.example.freeturilo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.freeturilo.core.RouteParameters;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    List<RouteParameters> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        history = RouteParameters.loadHistorySafe(this,
                new ToastExceptionHandler(this, R.string.no_history_message));
        HistoryAdapter adapter = new HistoryAdapter(this, history);
        ListView historyListView = findViewById(R.id.history_list);
        historyListView.setAdapter(adapter);
        historyListView.setOnItemClickListener(this::recreateRoute);
    }

    private void recreateRoute(AdapterView<?> adapterView, View view, int position, long id) {
        RouteParameters routeParameters = history.get(position);
        history.remove(position);
        RouteParameters.saveHistorySafe(this, history, new IgnoreExceptionHandler());
        Bundle bundle = new Bundle();
        bundle.putBinder(getString(R.string.route_parameters_intent_name), new ObjectWrapperForBinder(routeParameters));
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}