package com.loits.insurance.cm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.models.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.loits.insurance.cm.util.Constants.CAPTURE_IMAGE;
import static com.loits.insurance.cm.util.Constants.KEY_CHECK_GPS;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;

public class CameraCaptureActivity extends AppCompatActivity implements View.OnClickListener {

    static ArrayList<Uri> capturedUris = new ArrayList<Uri>();
    DatabaseHandler mDbHelper;
    ImageAdapter imageAdapter;
    private int intimationNo;
    private int inspectionType;
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        Button btnConfirm = (Button) findViewById(R.id.selectBtn);
        ImageButton btnCamera = (ImageButton) findViewById(R.id.imgBtnCam);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intimationNo = extras.getInt(KEY_IMG_INTIM);
            inspectionType = extras.getInt(KEY_IMG_INSP_TYPE);
        }

        btnConfirm.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        mDbHelper = new DatabaseHandler(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCapturedImages();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnCam: {
                checkGPS();
            }
            break;
            case R.id.selectBtn: {
                confirmTask();
            }
        }
    }

    private void confirmTask() {
        int cnt = 0;

        ArrayList<String> selectedImagePath = new ArrayList<String>();
        ArrayList<Double> imageLongitude = new ArrayList<Double>();
        ArrayList<Double> imagelatitude = new ArrayList<Double>();
        ArrayList<Long> imagedateTaken = new ArrayList<Long>();

        for (int i = 0; i < thumbnailsSelection.length; i++) {
            if (thumbnailsSelection[i]) {

                Uri uri = capturedUris.get(i);

                cnt++;

                selectedImagePath.add(getRealPathFromURI(uri));
                imageLongitude.add(getLongitudeFromURI(uri));
                imagelatitude.add(getLatitudeFromURI(uri));
                imagedateTaken.add(getDateTakenFromURI(uri));
            }
        }

        List<Image> images = mDbHelper.getAllImages(intimationNo, inspectionType);

        int imageCount = images.size();

        if ((imageCount + cnt) > Image.NO_OF_IMAGES) {

            Toast.makeText(
                    getApplicationContext(),
                    "You are not allowed to select more than "
                            + Image.NO_OF_IMAGES + " images.",
                    Toast.LENGTH_LONG).show();
            return;

        } else {

            // If success add extra values to the activity
            getIntent().putExtra("IMAGE_LIST", selectedImagePath);
            getIntent().putExtra("LNGT_LIST", imageLongitude);
            getIntent().putExtra("LTTD_LIST", imagelatitude);
            getIntent().putExtra("DATE_LIST", imagedateTaken);

            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    private void checkGPS() {

//        String provider = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(false);
        locationCriteria.setCostAllowed(true);
        locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String provider = locationManager.getBestProvider(locationCriteria, true);

        if (provider != null) {
            if (!provider.contains("gps")) {
                // Notify users and show settings if they want to enable GPS
                new AlertDialog.Builder(this)
                        .setMessage("GPS is switched off. enable?")
                        .setPositiveButton("Enable GPS",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(
                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(intent,
                                                KEY_CHECK_GPS);
                                    }
                                })
                        .setNegativeButton("Not Now",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "You cannot proceed without enabling GPS!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).show();
            } else {
                startCameraActivity();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    public static boolean isGpsEnabled(Context context) {

        /*if (PackageUtil.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }*/

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String providers = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (TextUtils.isEmpty(providers)) {
                return false;
            }
            return providers.contains(LocationManager.GPS_PROVIDER);
        } else {
            final int locationMode;
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            switch (locationMode) {

                case Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                case Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                    return true;
                case Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                case Settings.Secure.LOCATION_MODE_OFF:
                default:
                    return false;
            }
        }
    }


    private void addCapturedImage() {
        Uri uri = null;
        try {
            uri = Uri
                    .parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            .toString()
                            + File.separator
                            + getLastImageUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

            Log.v("Image Uri", uri.toString());
            capturedUris.add(uri);
        } catch (Exception e) {
            Log.v("Error adding image", e.toString());
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_IMAGE:

                if (resultCode == RESULT_OK) {
                    addCapturedImage();
                }
                break;

            case KEY_CHECK_GPS:
                if (resultCode == RESULT_CANCELED) {
                    String provider = Settings.Secure.getString(
                            getContentResolver(),
                            Settings.Secure.LOCATION_MODE);

                    if (provider != null) {
                        // Validate the geo-tagging is enabled or not
                        switch (provider.length()) {
                            case 0:
                                // GPS still not enabled..
                                Toast.makeText(this,
                                        "You cannot proceed without enabling GPS!",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                startCameraActivity();
                                break;
                        }
                    }
                } else {
                    Toast.makeText(this,
                            "You cannot proceed without enabling GPS!",
                            Toast.LENGTH_LONG).show();
                }

        }

    }

    private void loadCapturedImages() {

        Bitmap bm = null;
        this.count = capturedUris.size();
        this.thumbnailsSelection = new boolean[this.count];
        this.thumbnails = new Bitmap[capturedUris.size()];
        for (int i = 0; i < capturedUris.size(); i++) {
            try {
                Log.v("Load Image - Uri:", capturedUris.get(i)
                        .toString());

                bm = MediaStore.Images.Thumbnails.getThumbnail(
                        getContentResolver(),
                        getThumbnailIndex(capturedUris.get(i)),
                        MediaStore.Images.Thumbnails.MINI_KIND, null);


                thumbnails[i] = bm;

            } catch (Exception e) {
                Log.e("Error load Images", e.toString());
            }
        }

        GridView imageGrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imageGrid.setAdapter(imageAdapter);
    }

    public int getThumbnailIndex(Uri contentUri) {

        int res = 0;
        String[] proj = {MediaStore.Images.Media._ID};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            res = cursor.getInt(column_index);
        }
        cursor.close();
        return res;

    }

    public int getLastImageUri(Uri contentUri) {
        int res = 0;
        String[] proj = { MediaStore.Images.Media._ID };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToLast()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            res = cursor.getInt(column_index);
        }
        cursor.close();
        return res;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public double getLongitudeFromURI(Uri contentUri) {
        double res = 0;
        String[] proj = {MediaStore.Images.Media.LONGITUDE};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE);
            res = cursor.getDouble(column_index);
        }
        cursor.close();
        return res;
    }

    public double getLatitudeFromURI(Uri contentUri) {
        double res = 0;
        String[] proj = {MediaStore.Images.Media.LATITUDE};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE);
            res = cursor.getDouble(column_index);
        }
        cursor.close();
        return res;
    }

    public Long getDateTakenFromURI(Uri contentUri) {
        long res = 0;
        String[] proj = {MediaStore.Images.Media.DATE_TAKEN};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            res = cursor.getLong(column_index);
        }
        cursor.close();
        return res;
    }

    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gallery_item, null);
                holder.imageview = (ImageView) convertView
                        .findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsSelection[id]) {
                        cb.setChecked(false);
                        thumbnailsSelection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsSelection[id] = true;
                    }
                }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    int id = view.getId();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(
                            capturedUris.get(id), "image/*");
                    startActivity(intent);

                }
            });

            Glide.with(getApplicationContext())
                    .load(capturedUris.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.imageview);

            //holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(true);
            thumbnailsSelection[holder.checkbox.getId()] = true;

            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}
