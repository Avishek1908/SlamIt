package com.example.bhargavbv.ureon;

        import android.content.Intent;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.method.ScrollingMovementMethod;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Button;
        import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);
        imageView.setImageResource(R.drawable.profilepic);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                openGallery();
            }
        });
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
        }
    }
}
