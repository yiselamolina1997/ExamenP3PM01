package com.example.examenp3pm1;


import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FirebaseHelper {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    public FirebaseHelper() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void guardarEntrevista(Interview entrevista) {
        firestore.collection("entrevistas").add(entrevista);
    }

    public void obtenerEntrevistas() {
        firestore.collection("entrevistas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Interview> entrevistas = task.getResult().toObjects(Interview.class);
                    // mostrar la lista de entrevistas
                } else {
                    Log.d(TAG, "Error al obtener entrevistas");
                }
            }
        });
    }

    public void modificarEntrevista(Interview entrevista) {
        firestore.collection("entrevistas").document(entrevista.getIdOrden() + "").set(entrevista);
    }

    public void eliminarEntrevista(int idOrden) {
        firestore.collection("entrevistas").document(idOrden + "").delete();
    }

    public void subirImagen(byte[] image, String nombreImagen) {
        StorageReference storageRef = storage.getReference();
        StorageReference imagenRef = storageRef.child("imagenes/" + nombreImagen);
        imagenRef.putBytes(image);
    }

    public void subirAudio(byte[] audio, String nombreAudio) {
        StorageReference storageRef = storage.getReference();
        StorageReference audioRef = storageRef.child("audios/" + nombreAudio);
        audioRef.putBytes(audio);
    }
}