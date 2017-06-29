package com.loits.insurance.cm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {


    DatabaseHandler mDbHelper;

    private int intimationNo;
    private int inspectionType;
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private int[] arrid;
    private double[] arrlongitude;
    private double[] arrlatitude;
    private long[] arrdateTaken;

    Cursor imageCursor;
    ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);


        ImageButton btnCamera = (ImageButton) findViewById(R.id.imgBtnCam);
        btnCamera.setVisibility(ImageButton.INVISIBLE);

        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID, MediaStore.Images.Media.LONGITUDE,
                MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.DATE_TAKEN};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " desc";

        int image_column_index = 0;

        mDbHelper = new DatabaseHandler(getApplicationContext());


        imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        image_column_index = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            intimationNo = extras.getInt(KEY_IMG_INTIM);
            inspectionType = extras.getInt(KEY_IMG_INSP_TYPE);
        }

        count = imageCursor.getCount();

        thumbnails = new Bitmap[this.count];
        arrPath = new String[this.count];
        arrid = new int[this.count];
        arrlatitude = new double[this.count];
        arrlongitude = new double[this.count];
        arrdateTaken = new long[this.count];
        thumbnailsselection = new boolean[this.count];

        for (int i = 0; i < this.count; i++) {

            imageCursor.moveToPosition(i);
            int id = imageCursor.getInt(image_column_index);
            int dataColumnIndex = imageCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            int longColumnIndex = imageCursor
                    .getColumnIndex(MediaStore.Images.Media.LONGITUDE);
            int latdColumnIndex = imageCursor
                    .getColumnIndex(MediaStore.Images.Media.LATITUDE);
            int dateTakenColumnIndex = imageCursor
                    .getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

            arrPath[i] = imageCursor.getString(dataColumnIndex);
            arrid[i] = id;

            if (!imageCursor.isNull(latdColumnIndex)) {
                arrlatitude[i] = imageCursor.getDouble(latdColumnIndex);
            } else {
                arrlatitude[i] = 0;
            }

            if (!imageCursor.isNull(longColumnIndex)) {
                arrlongitude[i] = imageCursor.getDouble(longColumnIndex);
            } else {
                arrlongitude[i] = 0;
            }

            if (!imageCursor.isNull(dateTakenColumnIndex)) {
                arrdateTaken[i] = imageCursor.getLong(dateTakenColumnIndex);
            } else {
                arrdateTaken[i] = 0;
            }

        }

        GridView imageGrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imageGrid.setAdapter(imageAdapter);

        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectBtn: {
                int cnt = 0;

                ArrayList<String> selectedImagePath = new ArrayList<String>();
                ArrayList<Double> imageLongitude = new ArrayList<Double>();
                ArrayList<Double> imagelatitude = new ArrayList<Double>();
                ArrayList<Long> imagedateTaken = new ArrayList<Long>();

                for (int i = 0; i < thumbnailsselection.length; i++) {
                    if (thumbnailsselection[i]) {
                        cnt++;
                        selectedImagePath.add(arrPath[i]);
                        imageLongitude.add(arrlongitude[i]);
                        imagelatitude.add(arrlatitude[i]);
                        imagedateTaken.add(arrdateTaken[i]);
                        // selectImages = selectImages + arrPath[i] + "|";
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

                    getIntent().putExtra("IMAGE_LIST", selectedImagePath);
                    getIntent().putExtra("LNGT_LIST", imageLongitude);
                    getIntent().putExtra("LTTD_LIST", imagelatitude);
                    getIntent().putExtra("DATE_LIST", imagedateTaken);

                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();

                    Log.w(GalleryActivity.class.getName(), "IMG CLICK " + id + " // Count " + imageCursor.getCount());

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(
                            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id), "image/*");
                    startActivity(intent);

                }
            });

            File file = new File(arrPath[position]);

            /*Picasso.with(getApplicationContext())
                    .load(file)
                    .fit().centerCrop()
                    .into(holder.imageview);*/

            Glide.with(getApplicationContext())
                    .load(file)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.imageview);

            holder.checkbox.setChecked(thumbnailsselection[position]);

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
