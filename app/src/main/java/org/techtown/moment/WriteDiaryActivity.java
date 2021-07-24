package org.techtown.moment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.*;

public class WriteDiaryActivity extends AppCompatActivity {

    private static final String TAG="WriteDiary";

    ImageButton cameraBtn,imageBtn,mapBtn;
    ImageView imageView;
    File file;
    Button saveBtn;
    FloatingActionButton deleteBtn;
    LinearLayout pictureLayout;

    Bitmap resultBitmap;

    Context context;
    OnRequestListener requestListener;

    EditText contentsInput,addressInput;
    double latitude;
    double longitude;

    int REQUEST_IMAGE_CODE=111;
    int REQUEST_CAMERA_CODE=112;

    String photoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        context=this;

        if (context instanceof OnRequestListener) {
            requestListener = (OnRequestListener) context;
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        contentsInput=findViewById(R.id.writeDiaryEditText);
        imageView=findViewById(R.id.imageView);
        pictureLayout=findViewById(R.id.pictureLayout);
        addressInput=findViewById(R.id.addressEditWrite);

        cameraBtn=findViewById(R.id.btn_camera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        imageBtn=findViewById(R.id.btn_image);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mapBtn=findViewById(R.id.btn_map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new Map();
                String address =map.getAddress();
                if(address!=null) {
                    addressInput.setText(address);
                }
            }
        });

        saveBtn=findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             saveDiary();

             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);

            }
        });

        deleteBtn=findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture();
            }
        });
    }

    /*================File===============================*/


    private File createFile() {
        String filename = "capture.jpg";
        File databaseStr = new File(getExternalCacheDir(), filename);

        return databaseStr;
    }

    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = "IMG"+String.valueOf(curDate.getTime())+".jpg";

        return curDateStr;
    }



    //==============================Camera=========================================
    public void takePicture(){
        if (file==null){
            file=createFile();
        }
/*
        Uri fileUri= FileProvider.getUriForFile(this,"org.techtown.moment.fileprovider",file);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        }
*/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFileName = createFilename();
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), photoFileName);

            if (file != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "org.techtown.moment.fileprovider", file);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resultBitmap=null;

        if(requestCode==REQUEST_CAMERA_CODE&&resultCode==RESULT_OK){

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=6;
            resultBitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            pictureLayout.setVisibility(View.VISIBLE);
            resultBitmap=imageRotation(resultBitmap,file.getAbsolutePath());
            imageView.setImageBitmap(resultBitmap);


            Log.d("Picture", "picture 실행" );

        }else if(requestCode==REQUEST_IMAGE_CODE&&resultCode==RESULT_OK){
            Uri fileUri=data.getData();
            ContentResolver resolver=getContentResolver();

            try {
                InputStream inStream=resolver.openInputStream(fileUri);
                resultBitmap= BitmapFactory.decodeStream(inStream);


                pictureLayout.setVisibility(View.VISIBLE);

                imageView.setImageBitmap(resultBitmap);
                Log.d("Picture", "picture 실행" );

                inStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Bitmap imageRotation(Bitmap bitmap, String path){
        ExifInterface exif=null;

        try{
            exif=new ExifInterface(path);
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);

        Matrix matrix=new Matrix();

        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1,1);
                    break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.setRotate(180);
                        break;
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                            matrix.setRotate(180);
                            matrix.postScale(-1,1);
                            break;
                            case ExifInterface.ORIENTATION_TRANSPOSE:
                                matrix.setRotate(90);
                                matrix.postScale(-1,1);
                                break;
                                case ExifInterface.ORIENTATION_TRANSVERSE:
                                    matrix.setRotate(-90);
                                    matrix.postScale(-1,1);
                                    break;
                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        matrix.setRotate(-90);
                                        break;
            default:
                return bitmap;
        }
        try{
            Bitmap bmRotated=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            bitmap.recycle();
            return bmRotated;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }

    //========================Image===============================

    public void openGallery(){
        if (file==null){
            file=createFile();
        }
        Uri fileUri= FileProvider.getUriForFile(this,"org.techtown.moment.fileprovider",file);

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        startActivityForResult(intent,REQUEST_IMAGE_CODE);

    }

    /*====================Save===========================*/

    public String savePicture(){
        if(resultBitmap==null){
            return "";
        }

        File photoFolder =new File(AppConstants.FOLDER_PHOTO);

        if(!photoFolder.isDirectory()){
            Log.d(TAG,"creating photo folder : "+photoFolder);
            photoFolder.mkdir();
        }


        String picturePath=photoFolder+File.separator+ photoFileName;

        try{
            FileOutputStream outStream=new FileOutputStream(picturePath);
            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100,outStream);

            outStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return picturePath;
    }



    private void saveDiary() {
        String address = addressInput.getText().toString();
        if(addressInput.getText().toString().equals("주소")){
            address="";
        }

        String contents = contentsInput.getText().toString();

        String picturePath = savePicture();

        String sql = "Insert into " + DiaryDatabase.TABLE_DIARY +
                " ('ADDRESS', 'CONTENTS', 'PICTURE') values(" +
                "'"+ address + "', " +
                "'"+ contents + "', " +
                "'"+ picturePath + "');";

        Log.d(TAG, "sql : " + sql);
        DiaryDatabase database = DiaryDatabase.getInstance(context);
        database.exeSQL(sql);

        this.finish();
    }

    /*========================delete picture===========================*/


    public void deletePicture(){
        imageView.setImageBitmap(null);
        pictureLayout.setVisibility(View.GONE);

    }



    /*====================DiaryData====================

    public void setAddress(String data){
        locationTextView.setText(data);
    }

    public void setItem(DiaryData item){
        this.item=item;
    }

    public void setContents(String data) {
        contextsInput.setText(data);
    }

    public void setPicture(String picturePath,int SampleSize){
        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inSampleSize=SampleSize;
        resultBitmap=BitmapFactory.decodeFile(picturePath,options);

        imageView.setImageBitmap(resultBitmap);
    }

    public void applyItem(){

        AppConstants.println("applyItem called");
        if (item != null) {
            mMode = AppConstants.MODE_MODIFY;

            setAddress(item.getAddress());
            setContents(item.getContents());

            String picturePath = item.getPicture();
            if (picturePath == null || picturePath.equals("")) {
                imageView.setImageBitmap(null);
            } else {
                setPicture(item.getPicture(), 1);
            }


        } else {
            mMode = AppConstants.MODE_INSERT;

            setAddress("");

            Date currentDate = new Date();
            String currentDateString = AppConstants.dateFormat3.format(currentDate);

            contextsInput.setText("");

            imageView.setImageBitmap(null);
        }
    }
    */

    /*==========================Map===========================*/

    public class Map{

        //String Address;
        //List<android.location.Address> list=null;

        public Map(){

        }

        public String getAddress(){
            //startLocationService();
            //ReverseGeo();

            GpsTracker gpsTracker = new GpsTracker(WriteDiaryActivity.this);

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Toast.makeText(WriteDiaryActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();

            String address = getCurrentAddress(latitude, longitude);
            addressInput.setText(address);

            return address;
        }


        public String getCurrentAddress( double latitude, double longitude) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<android.location.Address> addresses;

            try {
                addresses = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        15);
            } catch (IOException ioException) {
                //네트워크 문제
                showToast("지오코더 서비스 사용불가");
                return "지오코더 서비스 사용불가";
            } catch (IllegalArgumentException illegalArgumentException) {
                showToast("잘못된 GPS 좌표");
                return "잘못된 GPS 좌표";

            }



            if (addresses == null || addresses.size() == 0) {
                showToast("주소 미발견");
                return "주소";

            }

            Address address = addresses.get(0);
            return address.getAddressLine(0).toString()+"\n";

        }

        class GpsTracker extends Service implements LocationListener {

            private final Context mContext;
            Location location;
            double latitude;
            double longitude;

            private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
            private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
            protected LocationManager locationManager;


            public GpsTracker(Context context) {
                this.mContext = context;
                getLocation();
            }


            public Location getLocation() {
                try {
                    locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                }
                catch (Exception e)
                {
                    Log.d("@@@", ""+e.toString());
                }

                return location;
            }

            public double getLatitude()
            {
                if(location != null)
                {
                    latitude = location.getLatitude();
                }

                return latitude;
            }

            public double getLongitude()
            {
                if(location != null)
                {
                    longitude = location.getLongitude();
                }

                return longitude;
            }

            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public IBinder onBind(Intent arg0) {
                return null;
            }


            public void stopUsingGPS()
            {
                if(locationManager != null)
                {
                    locationManager.removeUpdates(GpsTracker.this);
                }
            }


        }

/*
        class GPSListener implements LocationListener {
            public void onLocationChanged(Location location){
                showCurrentLocation(location);
            }
        }

        public void startLocationService(){
            LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

            long minTime=10000;
            float minDistance=0;
            GPSListener gpsListener=new GPSListener();

            try {
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        minTime,
                        minDistance,
                        gpsListener);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }

        public void showCurrentLocation(Location location){

            latitude=location.getLatitude();
            longitude=location.getLongitude();

            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.d("Map", message);
        }

        public void ReverseGeo(){
            try {
                list =geocoder.getFromLocation(
                        latitude,
                        longitude,
                        10
                );
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Map","입출력 오류 - 서버에서 주소 변환 에러");
            }
            if(list!=null){
                if(list.size()==0){
                    showToast("주소를 불러오는데 실패했습니다.");
                }
                else if(list.get(1)!=null){
                    Address=list.get(1).toString();
                }
                else{
                    Address=list.get(0).toString();
                }
            }
        }
         */

    }

    /*============================================================*/
    public void showToast(String message){
        makeText(this,message, LENGTH_SHORT).show();
    }
}