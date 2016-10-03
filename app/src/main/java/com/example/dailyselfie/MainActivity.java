package com.example.dailyselfie;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "DailySelfie";

    static final int REQUEST_PHOTO = 1;
    private static final long INITIAL_ALARM_DELAY = 60*1000;
    String mCurrentPhotoPath;
    ImageBadgeAdapter mAdapter;
    FileOutputStream out = null;
    File [] photos;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView imageListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ImageBadgeAdapter(getApplicationContext());
        imageListView.setAdapter(mAdapter);
        photos = getStoragedImages();
        if(photos!=null) {
            for (File f : photos) {
                    addNewPic(f);
                }
            }

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mNotificationReceiverIntent = new Intent(MainActivity.this,AlarmNotificationReceiver.class);
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mNotificationReceiverIntent, 0);

        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i(TAG,"Opened Large Image");

                String path = mAdapter.getItem(i).toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ImageLargeView.class);
                intent.putExtra("path",path);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_camera:
                takePicture();

                mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + INITIAL_ALARM_DELAY,
                        mNotificationReceiverPendingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent,REQUEST_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_PHOTO) && (resultCode == RESULT_OK)) {
            Bundle extras = data.getExtras();
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i(TAG, "file not created");
            }
            if (photoFile != null) {
                Bitmap bitmap = (Bitmap) extras.get("data");
                try {
                    out = new FileOutputStream(photoFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            addNewPic(photoFile);
        }
    }

    private File createImageFile () throws IOException{
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        String imageFileName = "PHOTO_" + timeStamp+"_";
        File storage = getFilesDir();
        File image = File.createTempFile(imageFileName,".png",storage);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void addNewPic(File image){
        if((image!=null) && (image.getName().endsWith(".png"))) mAdapter.add(image);
    }

    public File [] getStoragedImages(){
        String path = getFilesDir().toString();
        File folder = new File(path);
        return  folder.listFiles();
    }
}
