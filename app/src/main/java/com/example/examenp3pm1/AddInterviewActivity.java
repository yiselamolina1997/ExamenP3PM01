package com.example.examenp3pm1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddInterviewActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private Uri selectedImageUri;
    private Uri selectedAudioUri;

    private EditText etDescription, etJournalist, etDate;
    private ImageView imgPreview;
    private TextView tvAudioFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interview);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        etDescription = findViewById(R.id.etDescription);
        etJournalist = findViewById(R.id.etJournalist);
        etDate = findViewById(R.id.etDate);
        imgPreview = findViewById(R.id.imgPreview);
        tvAudioFileName = findViewById(R.id.tvAudioFileName);

        findViewById(R.id.btnSelectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        findViewById(R.id.btnSelectAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudio();
            }
        });

        findViewById(R.id.btnSaveInterview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInterview();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void selectAudio() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("audio/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                selectedImageUri = data.getData();
                imgPreview.setImageURI(selectedImageUri);
            } else if (requestCode == 2) {
                selectedAudioUri = data.getData();
                tvAudioFileName.setText(selectedAudioUri.getLastPathSegment());
            }
        }
    }

    private void saveInterview() {
        String description = etDescription.getText().toString().trim();
        String journalist = etJournalist.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (selectedImageUri == null || selectedAudioUri == null) {
            Toast.makeText(this, "Selecciona una imagen y un archivo de audio", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference imageRef = storage.getReference().child("images/" + selectedImageUri.getLastPathSegment());
        StorageReference audioRef = storage.getReference().child("audio/" + selectedAudioUri.getLastPathSegment());

        imageRef.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            audioRef.putFile(selectedAudioUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String audioUrl = uri.toString();

                                                Map<String, Object> interview = new HashMap<>();
                                                interview.put("description", description);
                                                interview.put("journalist", journalist);
                                                interview.put("date", date);
                                                interview.put("imageUrl", imageUrl);
                                                interview.put("audioUrl", audioUrl);

                                                db.collection("interviews")
                                                        .add(interview)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(AddInterviewActivity.this, "Entrevista guardada", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(AddInterviewActivity.this, "Error al guardar la entrevista", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(AddInterviewActivity.this, "Error al subir el archivo de audio", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(AddInterviewActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
