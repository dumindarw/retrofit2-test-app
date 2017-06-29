package com.loits.insurance.cm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.models.Image;
import com.loits.insurance.cm.util.FileCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;

public class GalleryViewActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseHandler mDbHelper;

    private int count;
    private Bitmap[] thumbnails;
    private long[] arrKey;
    private boolean[] thumbnailSselection;
    private int intimationNo;
    private int inspectionType;
    static File file;
    private String[] mStrings;
    private String[] imagesArr;
    private ImageAdapter imageAdapter;
    private final int VIEW_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        Button delBtn = (Button) findViewById(R.id.selectBtn);
        delBtn.setText(R.string.delete);
        ImageButton btnCamera = (ImageButton) findViewById(R.id.imgBtnCam);
        btnCamera.setVisibility(ImageButton.INVISIBLE);

        delBtn.setOnClickListener(this);

        mDbHelper = new DatabaseHandler(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            intimationNo = extras.getInt(KEY_IMG_INTIM);
            inspectionType = extras.getInt(KEY_IMG_INSP_TYPE);
        }

        loadImageIds();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectBtn:{
                for (int i = 0; i < thumbnailSselection.length; i++) {
                    if (thumbnailSselection[i]) {
                        mDbHelper.deleteImage(arrKey[i]);
                        setResult(RESULT_OK);
                        finish();
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case VIEW_IMAGE:
                if (resultCode == RESULT_CANCELED) {
                    //file.delete();
                }

        }
    }

    private void loadImageIds(){

        List<Image> images = mDbHelper.getAllImages(intimationNo, inspectionType);

        this.count = images.size();
        this.thumbnails = new Bitmap[this.count];
        this.arrKey = new long[this.count];
        this.thumbnailSselection = new boolean[this.count];

        mStrings = new String[images.size()];
        imagesArr = new String[images.size()];
        for(int i = 0; i<images.size();i++){
            mStrings[i] = ""+images.get(i).getId();
            arrKey[i] = images.get(i).getId();
            imagesArr[i] = images.get(i).getMyimage();
        }

        GridView imageGrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imageGrid.setAdapter(imageAdapter);
    }

    public Bitmap getImageBitmap(int id) {
        try {
            Bitmap imageBitmap = null;

            byte[] decodedString = Base64.decode(imagesArr[id], Base64.DEFAULT);

            ByteArrayInputStream is = new ByteArrayInputStream(decodedString);
            imageBitmap = BitmapFactory.decodeStream(is);

            return imageBitmap;

        } catch (IllegalArgumentException e) {
            Log.w(GalleryViewActivity.class.getName(), "IMAGE CURSOR Error: " + e.getMessage());
            return null;
        } catch (Exception e){
            e.printStackTrace();
            Log.w(IllegalArgumentException.class.getName(), "Image stream conversion error : " + e.getMessage());
            return null;
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

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gallery_item, null);
                holder.imageview = (ImageView) convertView
                        .findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailSselection[id]) {
                        cb.setChecked(false);
                        thumbnailSselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailSselection[id] = true;
                    }
                }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    final int id = view.getId();
                    // Bitmap abc = thumbnails[id];
                    Log.w(GalleryViewActivity.class.getName(), "id: " + id);

                    final ProgressDialog progressDialog = ProgressDialog.show(
                            GalleryViewActivity.this, "Please wait...", "Loading...",
                            true);

                    new Thread()
                    {
                        public void run() {

                            try {

                                File file = createTempFile(position);

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file),"image/*");
                                startActivityForResult(intent, VIEW_IMAGE);

                                progressDialog.dismiss();

                                //file.delete();

                            } catch (Exception e) {
                                Log.v("Other Error", e.toString());
                            }

                        }
                    }.start();
                }
            });

            Bitmap bmp = getImageBitmap(position);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] byteArray = stream.toByteArray();

            Glide.with(getApplicationContext())
                    .load(byteArray)
                    .asBitmap()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.imageview);

            if (thumbnailSselection.length > 0) {
                holder.checkbox.setChecked(thumbnailSselection[position]);
            }

            holder.id = position;

            //file.delete();

            return convertView;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private File createTempFile(int id){

        Bitmap abc = getImageBitmap(id);

        // File file = null;
        File cacheDir = null;

        FileCache fc = new FileCache(
                getApplicationContext());
        cacheDir = fc.getCacheDir();

        OutputStream fOut = null;

        try {
            file = File.createTempFile("Temp" + String.valueOf(id), ".jpg",  cacheDir);
            fOut = new FileOutputStream(file);
            if (abc.compress(Bitmap.CompressFormat.JPEG,
                    Image.VIEW_QUALITY, fOut))
                Log.w(GalleryViewActivity.class.getName(),
                        "File created...");

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.w(GalleryViewActivity.class.getName(),
                "Empty File created...");


        abc.recycle();

        return file;
    }



    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}
