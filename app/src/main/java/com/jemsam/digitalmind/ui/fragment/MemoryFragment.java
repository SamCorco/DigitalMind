package com.jemsam.digitalmind.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jemsam.digitalmind.model.Memory;
import com.jemsam.digitalmind.R;
import com.jemsam.digitalmind.model.Tag;
import com.jemsam.digitalmind.model.TagMemory;
import com.jemsam.digitalmind.model.User;
import com.jemsam.digitalmind.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.R.attr.button;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;


/**
 * Created by jeremy.toussaint on 19/10/16.
 */

public class MemoryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    public static final String TAG = MemoryFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA = 0;
    private static final int IMG_RESULT = 1;
    private static final String TEMP_FILE = "temp.jpg";
    private Memory memoryModel;
    private EditText titleEt;
    private EditText descriptionEt;
    private ImageView imageToAttach;
    private RatingBar ratingBar;
    private LinearLayout tagContainer;
    private User user;

    private GoogleMap googleMap;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11; //need this ???
    private GoogleApiClient mGoogleApiClient;

    private final LocationListener mLocationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG,"Location: " + location);
        }
    };
    private boolean allPositionRequested = true;

    public void setMemoryModel(Memory memoryModel) {
        this.memoryModel = memoryModel;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_detail, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        user = User.getUser(Utils.getPrefLogin(getContext()), Utils.getPrefPassword(getContext()));


        titleEt = (EditText) view.findViewById(R.id.title);
        if (memoryModel.getTitle() != null){
            titleEt.setText(memoryModel.getTitle());

        }

        descriptionEt = (EditText) view.findViewById(R.id.description);
        if (memoryModel.getDescription() != null){
            descriptionEt.setText(memoryModel.getDescription());

        }

        imageToAttach = (ImageView) view.findViewById(R.id.imageToAttach);
        if (memoryModel.getImagePath() != null){
            imageToAttach.setImageBitmap(BitmapFactory.decodeFile(memoryModel.getImagePath()));
        }

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setRating(memoryModel.getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "onRatingChanged: " + rating);
                memoryModel.setRating(rating);

            }
        });

        tagContainer = (LinearLayout) view.findViewById(R.id.tagContainer);
        appendAllTags();



        final EditText tagEd = (EditText) view.findViewById(R.id.tagEt);
        ImageButton addTagBtn = (ImageButton) view.findViewById(R.id.addTag);
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagContent = tagEd.getText().toString();
                Tag tag = Tag.getTag(tagContent, user);
                tag.linkMemory(memoryModel);
                appendAllTags();
                tagEd.setText("");
            }
        });



        ImageButton attachImageButton = (ImageButton) view.findViewById(R.id.attachImage);
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

        ToggleButton showAllMemories = (ToggleButton) view.findViewById(R.id.showAllMemories);
        showAllMemories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    List<Memory> memories = Memory.getAllMemories(user);

                    for( Memory memory : memories)
                    {

                        String title = memory.getTitle();
                        if (title == null){
                            title = "Empty title";
                        }
                        if (memory.getCoordinates().equals(memoryModel.getCoordinates())){
                            googleMap.addMarker(new MarkerOptions().position(memory.getCoordinates()).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        } else{
                            googleMap.addMarker(new MarkerOptions().position(memory.getCoordinates()).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        }
                    }

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(memoryModel.getCoordinates(), 0));
                } else {
                    googleMap.clear();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(memoryModel.getCoordinates(), 8));
                    googleMap.addMarker(new MarkerOptions().position(memoryModel.getCoordinates()).title("Your current memory is here !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                }

            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Memory \"" + memoryModel.getTitle() + "\" extracted from my mind:\n\n" + memoryModel.getDescription());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Spread your mind!"));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void appendAllTags() {
        tagContainer.removeAllViews();
        List<TagMemory> tagMemories = TagMemory.getAllTagMemories();

        for (TagMemory tagMemory: tagMemories){
            if (tagMemory.getMemoryId().equals(memoryModel.getId())){
                TextView tv = new TextView(getContext());
                Tag tag = Tag.getTag(tagMemory.getTagId());
                if (tag != null){
                    tv.setText("#" + tag.getTagContent());
                    tagContainer.addView(tv);
                }

            }
        }

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_RESULT);
    }

    private void openCamera() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider",getTempFile()));//Uri.fromFile(getTempFile())
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private File getTempFile(){
        //it will return /sdcard/image.tmp
        final File path = new File( Environment.getExternalStorageDirectory(), getContext().getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(MAP_TYPE_NORMAL);
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        memoryModel.setTitle(titleEt.getText().toString());
        memoryModel.setDescription(descriptionEt.getText().toString());
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
                getImageFromCamera(data);
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

    private void getImageFromCamera(Intent data) {
        final File file = getTempFile();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider",file));//Uri.fromFile(file)
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        byte[] byteArray = stream.toByteArray(); // convert camera photo to byte array
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(file);
            fo.write(byteArray);
            fo.flush();
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        memoryModel.setImagePath(file.getAbsolutePath());
        imageToAttach.setImageBitmap(bitmap);
    }

    @Override
    public void onConnected(Bundle bundle) {

        LatLng userLocation;

        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);

        if ( ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, null, mLocationListener);
        } else {
            if (memoryModel.getCoordinates() == null){
                memoryModel.setCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }

        if (memoryModel.getCoordinates() != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(memoryModel.getCoordinates(), 15));
            googleMap.addMarker(new MarkerOptions().position(memoryModel.getCoordinates()).title("Your current memory is here !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
