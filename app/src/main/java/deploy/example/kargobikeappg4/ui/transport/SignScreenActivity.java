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

    //Attributes
    private PaintView paintView;
    public Bitmap signature;
    private Button btnSave;
    public String imageUrl;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_screen);

        //Initilize the scrren for Sign
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

    //Go one page back
    public void onBackPressed(){
        super.onBackPressed();
    }


    //Save the Image in the Firebase storage
    public void save(){
        signature = paintView.getImage();

        //Create path for the image
        String orderId = getIntent().getStringExtra("IdOrder");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://kargobike-group4.appspot.com");
        StorageReference signatureRef = storageRef.child("signatures/"+orderId + "_signature.jpg");
        StorageReference signatureImageRef = storageRef.child("signatures/"+orderId + "_signature.jpg");

        signatureRef.getName().equals(signatureImageRef.getName());
        signatureRef.getPath().equals(signatureImageRef.getPath());

        //convert the image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = signatureRef.putBytes(data);

        //Save the image
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
            imageUrl = downloadUrl.toString();
            Log.d("IMAGE URL", "the URL is: " + imageUrl);
            Intent intent = new Intent();
            intent.putExtra("SignatureURL", imageUrl);
            setResult(RESULT_OK, intent);
            onBackPressed();

        });
    }

}
