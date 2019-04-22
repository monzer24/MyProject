package com.checks.admin.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.checks.admin.CheckAdapter;
import com.checks.admin.R;
import com.checks.admin.model.Check;
import java.util.List;

public class CashedChecks extends Activity {

    private RecyclerView checksView;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashed_checks);
        List<Check> checks = (List<Check>) getIntent().getSerializableExtra("checks");
        CheckAdapter adapter = new CheckAdapter(checks, this);

        if(checks != null && checks.size() > 0) {
            checksView = findViewById(R.id.r_view);
            manager = new LinearLayoutManager(this);
            checksView.setHasFixedSize(true);
            checksView.setLayoutManager(manager);
            checksView.setAdapter(adapter);
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.check)
                    .setTitle("No Cashed Checks !")
                    .setMessage("No cashed checks for now, good job !")
                    .setNegativeButton("Exit",null)
                    .show();
        }
    }
}
