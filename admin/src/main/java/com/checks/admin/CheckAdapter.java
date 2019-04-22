package com.checks.admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.checks.admin.model.Check;
import com.checks.admin.ui.CheckInfo;
import java.io.Serializable;
import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.CheckViewHolder> implements Serializable {

    private List<Check> checks;
    private Context context;

    public CheckAdapter(List<Check> checks, Context context) {
        this.checks = checks;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.check_list, viewGroup, false);
        return new CheckViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder checkViewHolder, final int i) {
        checkViewHolder.id.setText(checks.get(i).getId());
        checkViewHolder.name.setText(checks.get(i).getRecipient().getFullName());
        checkViewHolder.amount.setText(checks.get(i).getAmount());
        checkViewHolder.date.setText(checks.get(i).getDate());

        checkViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, CheckInfo.class);
                in.putExtra("check", checks.get(i));
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checks.size();
    }

    public class CheckViewHolder extends RecyclerView.ViewHolder {

        private View v;
        private Button card;
        private TextView id;
        private TextView name;
        private TextView amount;
        private TextView date;

        public CheckViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.checkid);
            name = itemView.findViewById(R.id.recipientName);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.more);
        }
    }
}
