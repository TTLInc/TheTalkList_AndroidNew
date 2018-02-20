package com.ttl.project.thetalklist;

import android.app.*;
import android.content.pm.PackageManager;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Video record class
public class VideoRecord extends Fragment {


    ImageView upload_video, upload_video_gallery;


    final int SELECT_VIDEO = 13210;


    View view1;
    View view;
    android.widget.VideoView VDOView;
    Dialog dialog;
    SharedPreferences bio_videoPref;


    private SharedPreferences permissionStatus;

    final String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;


    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 20000;
    public static final SettingFlyout ActivityContext = null;


    Spinner subject;

    EditText videoRecord_title, videoRecord_desc;
    String current_file_apth;
    TextView timeDuration;
    ProgressBar progressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_record_layout, container, false);
        permissionStatus = getContext().getSharedPreferences("permission status", 0);
        checkPermission();

        upload_video = (ImageView) view.findViewById(R.id.upload_video);
        upload_video_gallery = (ImageView) view.findViewById(R.id.upload_video_gallery);
        subject = (Spinner) view.findViewById(R.id.videorecoedspnsubject);
        videoRecord_title = (EditText) view.findViewById(R.id.videoRecord_title);
        videoRecord_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        videoRecord_desc = (EditText) view.findViewById(R.id.videoRecord_desc);
        videoRecord_desc.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        bio_videoPref = getContext().getSharedPreferences("bio_video", Context.MODE_PRIVATE);

        if (bio_videoPref.getBoolean("biography", false)) {
            view.findViewById(R.id.video_upload_control).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.videorecord_txt)).setText("Upload your 1 min video to be connected to your profile.");
        }

        progressBar = new ProgressBar(getContext());

        {
            RequestQueue queue1 = Volley.newRequestQueue(getContext());
            String URL = "https://www.thetalklist.com/api/subject";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("subjects");
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("Select Subject");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("subject"));
                        }
                        if (getActivity() != null) {
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_gray_textview, coun);
                            subject.setAdapter(arrayAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
            );
            queue1.add(getRequest);
        }
        timeDuration = (TextView) view.findViewById(R.id.timeDuration);


        view1 = inflater.inflate(R.layout.popup_video_upload_layout, null);


        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_video_upload_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);


        VDOView = (VideoView) dialog.findViewById(R.id.video111);


        upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bio_videoPref.getBoolean("biography", false)) {

                    if (subject.getSelectedItem().toString().equalsIgnoreCase("select subject")) {
                        Toast.makeText(getContext(), "Please select subject ", Toast.LENGTH_SHORT).show();
                    } else if (videoRecord_title.getText().toString().matches("")) {
                        Toast.makeText(getContext(), "Please fill Title ", Toast.LENGTH_SHORT).show();
                    } else if (videoRecord_title.length() > 40) {
                        Toast.makeText(getContext(), "Title's size must be minimum 40 characters ", Toast.LENGTH_SHORT).show();
                    } else if (videoRecord_desc.getText().toString().matches("")) {

                        Toast.makeText(getContext(), "Please fill Description ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ns/");
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {
                                Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                                        Toast.LENGTH_LONG).show();
                                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                            }
                        }
                        java.util.Date date = new java.util.Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(date.getTime());
                        File mediaFile;
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                                "VID_" + timeStamp + ".mp4");
                        current_file_apth = mediaStorageDir.getPath() + File.separator +
                                "VID_" + timeStamp + ".mp4";
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ns/");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                                    Toast.LENGTH_LONG).show();
                            Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                        }
                    }
                    java.util.Date date = new java.util.Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(date.getTime());
                    File mediaFile;
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
                    current_file_apth = mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                }

            }
        });

        upload_video_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!bio_videoPref.getBoolean("biography", false)) {
                    if (subject.getSelectedItem().toString().equalsIgnoreCase("select subject")) {
                        Toast.makeText(getContext(), "Please select subject ", Toast.LENGTH_SHORT).show();
                    } else if (videoRecord_title.getText().toString().matches("")) {
                        Toast.makeText(getContext(), "Please fill Title ", Toast.LENGTH_SHORT).show();
                    } else if (videoRecord_desc.getText().toString().matches("")) {

                        Toast.makeText(getContext(), "Please fill Description ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);

                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                }
            }
        });


        final SharedPreferences pref = getContext().getSharedPreferences("uploadVideo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        return view;
    }

    //Check permission
    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.RECORD_AUDIO)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                editor.apply();


            } else {
                proceedAfterPermission();
            }
        } else {
        }
    }
//Method after permission
    private void proceedAfterPermission() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[3])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            final Uri image_uri = data.getData();
            if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            }
            if (requestCode == SELECT_VIDEO) {


                launchUploadActivity(generatePath(image_uri, getContext()));


            }
            if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {


                launchUploadActivity(generatePath(image_uri, getContext()));

            }
//            getActivity().onBackPressed();
        }

    }


    //Launch video upload activity
    private void launchUploadActivity(String isImage) {
        Intent i = new Intent(getContext(), UploadActivity.class);
        i.putExtra("filePath", isImage);
        i.putExtra("id", getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0));
        i.putExtra("subject", subject.getSelectedItem().toString());
        i.putExtra("title", videoRecord_title.getText().toString());
        i.putExtra("description", videoRecord_desc.getText().toString());
        i.putExtra("activity", getActivity().getClass().toString());

        SharedPreferences.Editor editor=bio_videoPref.edit();



        if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {
//            getFragmentManager().beginTransaction().remove(new VideoRecord()).commit();
//            editor.putBoolean("fromReg",true).apply();
            startActivity(i);
//            if (getActivity() != null)
//                getActivity().onBackPressed();
        } else {
            if (bio_videoPref.getBoolean("biography", false)) {
//                if (getActivity() != null)
//                    getActivity().onBackPressed();
                startActivity(i);
            } else startActivity(i);
        }
//            startActivity(i);
    }


    /**
     * Method to show alert dialog
     */


    //Generate path of the video
    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }



}
