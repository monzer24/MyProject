package com.checks.admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CashedChecks extends Activity {

    private RecyclerView checksView;
    private List<Check> checksList;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashed_checks);
        List<Map<String, Object>> checks = (List<Map<String, Object>>) getIntent().getBundleExtra("checks").getSerializable("checks");
        CheckAdapter adapter = new CheckAdapter(checks, this);

        System.out.println(checks.size());
        checksView  = findViewById(R.id.r_view);
        manager = new LinearLayoutManager(this);
        checksView.setHasFixedSize(true);
        checksView.setLayoutManager(manager);
        checksView.setAdapter(adapter);
    }
}
