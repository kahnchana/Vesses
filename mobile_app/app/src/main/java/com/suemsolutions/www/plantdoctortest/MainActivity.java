package com.suemsolutions.www.plantdoctortest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    private static final int SELECT_IMAGE = 0;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout)findViewById(R.id.layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.VISIBLE);
            }
        });
    }



    public void launchCamera(View view){
        layout.setVisibility(View.GONE);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    public void launchGallery(View view){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Picture"),SELECT_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //send to the server code
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray = stream.toByteArray();

            getServerResponse(byteArray);

            //Intent intent = new Intent(getBaseContext(),Main2Activity.class);
            //intent.putExtra("bitmapID",byteArray);
            //startActivity(intent);

        }else if (requestCode==SELECT_IMAGE && resultCode==Activity.RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                System.out.println(imageUri);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();

                getServerResponse(byteArray,imageUri);

                //Intent intent = new Intent(getBaseContext(),Main2Activity.class);
                //intent.putExtra("bitmapID",byteArray);
                //startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("FileNotFoundException");
            }
        }
    }

    private void getServerResponse(final byte[] byteArray, final Uri imageUri) {
        RequestParams params = new RequestParams();
        params.put("image", new ByteArrayInputStream(byteArray), "image.jpg");
        Log.d("HTTP","Params READY!!!");

        try {
            HttpClient.post("upload",params,new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    Intent intent = new Intent(getBaseContext(),Main2Activity.class);
                    intent.putExtra("imageURI",imageUri.toString());
                    intent.putExtra("response",response);
                    startActivity(intent);
                }
            });
        }catch (Exception e){

        }

    }

    private void getServerResponse(final byte[] byteArray) {
        RequestParams params = new RequestParams();
        params.put("image", new ByteArrayInputStream(byteArray), "image.jpg");
        Log.d("HTTP","Params READY!!!");

        try {
            HttpClient.post("upload",params,new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    Intent intent = new Intent(getBaseContext(),Main2Activity.class);
                    intent.putExtra("byteArray",byteArray);
                    intent.putExtra("response",response);
                    startActivity(intent);
                }
            });
        }catch (Exception e){

        }

    }
}
