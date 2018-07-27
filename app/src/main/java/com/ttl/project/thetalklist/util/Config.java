package com.ttl.project.thetalklist.util;

//Config file for video uploading

import android.widget.TextView;

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "https://www.thetalklist.com/api/video_upload_test";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static int msgCount;
    public static int sumbitMsg;
    public static TextView bottombar_message_count;
}
