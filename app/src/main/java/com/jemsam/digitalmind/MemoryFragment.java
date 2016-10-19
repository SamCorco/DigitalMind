package com.jemsam.digitalmind;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;


/**
 * Created by jeremy.toussaint on 19/10/16.
 */

public class MemoryFragment extends Fragment {

    public static final String TAG = MemoryFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA = 0;
    private static final int IMG_RESULT = 1;
    private static final String TEMP_FILE = "temp.jpg";
    private Memory memoryModel;
    private EditText titleEd;
    private EditText descriptionEd;
    private ImageView imageToAttach;

    public void setMemoryModel(Memory memoryModel) {
        this.memoryModel = memoryModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_detail, container, false);

        titleEd = (EditText) view.findViewById(R.id.title);
        titleEd.setText(memoryModel.getTitle());

        descriptionEd = (EditText) view.findViewById(R.id.description);
        descriptionEd.setText(memoryModel.getDescription());

        imageToAttach = (ImageView) view.findViewById(R.id.imageToAttach);
        if (memoryModel.getImagePath() != null){
            imageToAttach.setImageBitmap(BitmapFactory.decodeFile(memoryModel.getImagePath()));
        }

        Button attachImageButton = (Button) view.findViewById(R.id.attachImage);
        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {getString(R.string.dialog_take_photo_item), getString(R.string.dialog_choose_image_item)};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.dialog_add_image));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].toString().equals(getString(R.string.dialog_choose_image_item))){
                            openImagePicker();
                        } else {
                            openCamera();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_RESULT);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), TEMP_FILE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onDestroy() {
        memoryModel.setTitle(titleEd.getText().toString());
        memoryModel.setDescription(descriptionEd.getText().toString());
        Memory.update(memoryModel);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {
                getImageFromGallery(data);
            } else if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
                getImageFromCamera();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void getImageFromGallery(Intent data) {
        Uri URI = data.getData();
        String[] FILE = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(URI, FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String imagePath = cursor.getString(columnIndex);
        imageToAttach.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        memoryModel.setImagePath(imagePath);
        cursor.close();
    }

    private void getImageFromCamera() {
        File f = new File(Environment.getExternalStorageDirectory().toString());
        for (File temp : f.listFiles()) {
            if (temp.getName().equals(TEMP_FILE)) {
                f = temp;
                break;
            }
        }
        try {
            Bitmap bm;
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            bm = BitmapFactory.decodeFile(f.getAbsolutePath(),btmapOptions);
            String path = Environment.getExternalStorageDirectory()
                    + File.separator
                    + getContext().getString(R.string.app_name);

            f.delete();
            OutputStream fOut = null;
            File dir = new File(path);
            dir.mkdirs();
            File file = new File(dir,String.valueOf(System.currentTimeMillis()) + ".jpg");
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            memoryModel.setImagePath(file.getAbsolutePath());
            imageToAttach.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
