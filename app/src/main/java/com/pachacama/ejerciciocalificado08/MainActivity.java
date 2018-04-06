package com.pachacama.ejerciciocalificado08;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private static final int PERMISSIONS_REQUEST = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image = findViewById(R.id.profile_image);

    }


    public void TakePhoto(View view){


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST);


        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(intent, 1);

            }
        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        EasyImage.openCamera(MainActivity.this, 0);
                    }else{
                        Toast.makeText(this, "CAMERA permisos denegados!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }



    }


        @Override
        protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                    e.printStackTrace();
                }

                @Override
                public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {

                    Toast.makeText(MainActivity.this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();

                    Picasso.get().load(imageFiles.get(0)).into(profile_image);
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                        if (photoFile != null) photoFile.delete();
                    }
                }

            });
    }

}
