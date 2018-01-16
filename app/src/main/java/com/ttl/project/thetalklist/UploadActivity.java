package com.ttl.project.thetalklist;


import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ttl.project.thetalklist.util.AndroidMultiPartEntity;

public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = UploadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;

    ProgressDialog progressDialog;

    int id;
    String subject;
    String title;
    String description;
    String activity;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);

        Intent i = getIntent();

        filePath = i.getStringExtra("filePath");
        id = i.getIntExtra("id", 0);
        subject = i.getStringExtra("subject");
        title = i.getStringExtra("title");
        description = i.getStringExtra("description");
        activity=i.getStringExtra("activity");


        previewMedia();


        findViewById(R.id.btnUpload_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                new SettingFlyout().onBackPressed();
//                onBackPressed();


            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(UploadActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                String response = String.valueOf(new UploadFileToServer().execute());
                Log.e("video response vstring", response);
            }
        });

    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia() {
        // Checking whether captured media is image or video

        vidPreview.setVisibility(View.VISIBLE);
        vidPreview.setVideoPath(filePath);
        // start playing
        vidPreview.start();

    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        String responseString;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog.dismiss();
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://www.thetalklist.com/api/video_upload_test");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("video", new FileBody(sourceFile));
                entity.addPart("uid", new StringBody(String.valueOf(id)));
                entity.addPart("subject", new StringBody(subject));
                entity.addPart("title", new StringBody(title));
                entity.addPart("description", new StringBody(description));

                SharedPreferences bio_videoPref = getApplicationContext().getSharedPreferences("bio_video", Context.MODE_PRIVATE);
                SharedPreferences.Editor bio_Editor = bio_videoPref.edit();

                if (bio_videoPref.getBoolean("biography", false))
                    entity.addPart("bio", new StringBody("1"));
                else entity.addPart("bio", new StringBody("0"));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                    Log.e("video response", responseString);
                    bio_Editor.clear().apply();
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            try {
                JSONObject jsonObject = new JSONObject(responseString);
                if (jsonObject.getInt("status") == 1) {
                    Toast.makeText(UploadActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(UploadActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            onBackPressed();

            if (activity.equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {

                Intent i = new Intent(getApplicationContext(), Registration.class);
                i.putExtra("back", true);
                startActivity(i);
                finish();
            } else {


                Intent i = new Intent(getApplicationContext(), SettingFlyout.class);
                i.putExtra("back", true);
                startActivity(i);
                finish();
            }
            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */

}