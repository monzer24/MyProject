package com.checks.admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.CheckViewHolder> implements Serializable {

    private List<Map<String, Object>> checks;
    private Context context;

    public CheckAdapter(List<Map<String, Object>> checks, Context context) {
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
        final Check check = (Check) checks.get(i).get("check");
        checkViewHolder.id.setText(checks.get(i).get("id").toString());
//        checkViewHolder.name.setText(check.getRecipientName());
        checkViewHolder.amount.setText(check.getAmount());
        checkViewHolder.date.setText(check.getCheckDate());

        checkViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, CheckInfo.class);
                Check check = (Check) checks.get(i).get("check");
                in.putExtra("check", check);
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
        private CardView card;
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
            card = itemView.findViewById(R.id.card);
        }
    }
}
