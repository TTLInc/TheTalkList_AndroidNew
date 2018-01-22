package com.ttl.project.thetalklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edmodo.cropper.CropImageView;
import com.ttl.project.thetalklist.Services.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saubhagyam on 22/09/2017.
 */

public class Fragment_cropImage extends AppCompatActivity {

    Bitmap uri;
    BroadcastReceiver finish1;
    ImageView rotate;

    public Fragment_cropImage(Bitmap uri) {
        this.uri = uri;
    }

    public Fragment_cropImage() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(finish1);
    }
    int rotating=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_fragment);

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(350, 400);
        cropImageView.setGuidelines(2);
        final Button crop= (Button) findViewById(R.id.crop);
        final Button set= (Button) findViewById(R.id.set);

        cropImageView.setRotation(0);
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        uri= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        final ImageView imageCrop=((ImageView)findViewById(R.id.imageCrop));
        rotate= (ImageView) findViewById(R.id.rotate);
        cropImageView.setImageBitmap(uri);


        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageCrop.setImageBitmap(uri);

                cropImageView.setVisibility(View.GONE);
                imageCrop.setVisibility(View.VISIBLE);

                imageCrop.setRotation(imageCrop.getRotation()+90);


                rotating=1;
                crop.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);
            }
        });


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cropImageView.setVisibility(View.VISIBLE);
                cropImageView.setImageBitmap(null);
                cropImageView.setImageBitmap(getScreenShot(imageCrop));
                imageCrop.setVisibility(View.GONE);

                set.setVisibility(View.GONE);
                crop.setVisibility(View.VISIBLE);


            }
        });



        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.setVisibility(View.VISIBLE);
                Bitmap imageBitmap = cropImageView.getCroppedImage();
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                imageCrop.setRotation(0);
                imageCrop.setImageBitmap(imageBitmap);
                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);

                Bitmap bb=((BitmapDrawable)imageCrop.getDrawable()).getBitmap();
                Bitmap cc=RotateBitmap(bb,0);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                cc.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                uploadImage(encodedImageString, ((BitmapDrawable) imageCrop.getDrawable()).getBitmap(), getApplicationContext(), pref.getInt("id", 0));


            }
        });

    }



    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void uploadImage(final String encodedImageString, final Bitmap bitmap, final Context context, final int id) {





        String uploadURL = "https://www.thetalklist.com/api/profile_pic";
        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);
        Log.e("encoded image string ", encodedImageString);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();






        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

//                Toast.makeText(context, "Some error occurred! " + s, Toast.LENGTH_LONG).show();

                Log.e("image upload response",s);
                try {
                    JSONObject res=new JSONObject(s);
                    if (res.getInt("status")==0){
                        LoginService loginService=new LoginService();
                        loginService.login(context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("email",""),context.getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getString("pass",""),context);

                        Toast.makeText(context, "Image uploaded!", Toast.LENGTH_SHORT).show();


                        onBackPressed();


                    }else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", encodedImageString);
                parameters.put("uid", String.valueOf(id));
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
