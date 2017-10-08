package com.example.bhargavbv.ureon;

        import android.content.ContentResolver;
        import android.content.Intent;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.method.ScrollingMovementMethod;
        import android.util.Log;
        import android.view.View;
        import android.webkit.MimeTypeMap;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends AppCompatActivity {
    ImageView imageView;
    Button button,setasdp;
    boolean selected =  false;

    private StorageReference sref;
    private DatabaseReference dref;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);
        setasdp = (Button)findViewById(R.id.setnewdp);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        imageView.setImageResource(R.drawable.profilepic);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                openGallery();
            }
        });

        sref = FirebaseStorage.getInstance().getReference();
        dref  = FirebaseDatabase.getInstance().getReference();

        setasdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected)
                {

                    Log.i("Hello",imageUri.toString());
                    sref.child("dp").child(System.currentTimeMillis() + "." + getFileExtension(imageUri)).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user!=null;
                            String uid = user.getUid();
                            dref.child("users").child(uid).child("photo").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    //show progress
                                    double progress = (100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();


                                }
                            });
                }
            }
        });
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(null);
            imageView.setImageURI(imageUri);
            selected = true;
        }
    }
}
