package deploy.example.kargobikeappg4.ui.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.ui.transport.paint.PaintView;

public class SignScreenActivity extends AppCompatActivity{
    private PaintView paintView;
    public Bitmap signature;
    private Button btnSave;
    public String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_screen);

        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        btnSave = findViewById(R.id.btn_signature_ok);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

            }
        });
    }

    private PaintView getUrl(){

            //Add click listener, opens details of the selected act




        return this.paintView;
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    public void save(){
        signature = paintView.getImage();
        String orderId = getIntent().getStringExtra("IdOrder");
        Log.d("ORDER-ID", "Order id = " + orderId);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://kargobike-group4.appspot.com");
        StorageReference signatureRef = storageRef.child("signatures/"+orderId + "_signature.jpg");
        StorageReference signatureImageRef = storageRef.child("signatures/"+orderId + "_signature.jpg");

        signatureRef.getName().equals(signatureImageRef.getName());
        signatureRef.getPath().equals(signatureImageRef.getPath());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = signatureRef.putBytes(data);


        Log.d("IMAGE URL", "the 1 URL is: " + imageUrl);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override

            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                imageUrl = downloadUrl.toString();
                Log.d("IMAGE URL", "the URL is: " + imageUrl);
                Intent intent = new Intent();
                intent.putExtra("SignatureURL", imageUrl);
                setResult(RESULT_OK, intent);
                onBackPressed();

            }
        });
        Log.d("IMAGE URL", "the last URL is: " + imageUrl);


    }




}
