package com.example.examenp3pm1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class InterviewDetailActivity extends AppCompatActivity {

    private TextView tvDescription;
    private ImageView imgInterview;
    private MediaPlayer mediaPlayer;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_detail);

        tvDescription = findViewById(R.id.tvDescription);
        imgInterview = findViewById(R.id.imgInterview);

        Interview interview = (Interview) getIntent().getSerializableExtra("interview");
        tvDescription.setText(interview.getDescription());
        Picasso.get().load(interview.getImageUrl()).into(imgInterview);
        audioUrl = interview.getAudioUrl();

        findViewById(R.id.btnPlayAudio).setOnClickListener(v -> playAudio());
    }

    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
