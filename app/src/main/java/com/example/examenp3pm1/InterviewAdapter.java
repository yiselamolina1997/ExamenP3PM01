package com.example.examenp3pm1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.ViewHolder> {

    private List<Interview> interviews;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Interview interview);
    }

    public InterviewAdapter(List<Interview> interviews, OnItemClickListener listener) {
        this.interviews = interviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Interview interview = interviews.get(position);
        holder.tvDescription.setText(interview.getDescription());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(interview));
    }

    @Override
    public int getItemCount() {
        return interviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
