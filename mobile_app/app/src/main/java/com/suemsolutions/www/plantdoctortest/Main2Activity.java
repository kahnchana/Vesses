package com.suemsolutions.www.plantdoctortest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Main2Activity extends AppCompatActivity {
    private List<Disease> diseaseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DiseasesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);




        /*
        byte[] byteArray = getIntent().getByteArrayExtra("bitmapID");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        imageView.setImageBitmap(bitmap);
        */


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DiseasesAdapter(diseaseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //getServerResponse(byteArray);
        String response=getIntent().getStringExtra("response");//
        updateList(response);

        String imageURI=getIntent().getStringExtra("imageURI");
        System.out.println(imageURI);
        if(imageURI != null){
            imageView.setImageURI(Uri.parse(imageURI));
        }

    }

    private void updateList(String response) {
        JsonParser parser = new JsonParser();
        JsonElement result = parser.parse(response);
        if (result.isJsonObject()) {
            JsonObject obj = result.getAsJsonObject();
            JsonArray arr = obj.get("probabilities").getAsJsonArray();

            for (int i = 0; i < arr.size(); i++) {
                JsonObject d_obj = arr.get(i).getAsJsonObject();
                System.out.println(d_obj.get("disease").getAsString());
                System.out.println(d_obj.get("probability").getAsFloat());
                Disease disease=new Disease(d_obj.get("disease").getAsString(),d_obj.get("probability").getAsInt());
                diseaseList.add(disease);
            }

        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void getServerResponse(byte[] byteArray) {
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
                    JsonParser parser = new JsonParser();
                    JsonElement result = parser.parse(response);
                    if (result.isJsonObject()) {
                        JsonObject obj = result.getAsJsonObject();
                        JsonArray arr = obj.get("probabilities").getAsJsonArray();

                        for (int i = 0; i < arr.size(); i++) {
                            JsonObject d_obj = arr.get(i).getAsJsonObject();
                            System.out.println(d_obj.get("disease").getAsString());
                            System.out.println(d_obj.get("probability").getAsFloat());
                            Disease disease=new Disease(d_obj.get("disease").getAsString(),d_obj.get("probability").getAsInt());
                            diseaseList.add(disease);
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e){

        }

    }
    */
}
