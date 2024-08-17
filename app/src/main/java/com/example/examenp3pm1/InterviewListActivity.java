package com.example.examenp3pm1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InterviewListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InterviewAdapter adapter;
    private List<Interview> interviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InterviewAdapter(interviewList, new InterviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Interview interview) {
                Intent intent = new Intent(InterviewListActivity.this, InterviewDetailActivity.class);
                intent.putExtra("interview", interview);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        loadInterviews();
    }

    private void loadInterviews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("interviews").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            interviewList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Interview interview = document.toObject(Interview.class);
                                interviewList.add(interview);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
