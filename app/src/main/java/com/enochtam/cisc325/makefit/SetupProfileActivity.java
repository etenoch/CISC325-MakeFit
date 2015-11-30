package com.enochtam.cisc325.makefit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetupProfileActivity extends Activity {

    private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.enochtam.cisc325.makefit.fileprovider";

    Data dao;
    SharedPreferences prefs;


    @Bind(R.id.first_name) EditText firstName;
    @Bind(R.id.last_name) EditText lastName;
    @Bind(R.id.create_profile) Button createProfileButton;
    @Bind(R.id.profile_image) CircleImageView profileImage;


    Uri imageUri;
    String imagePath;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        ButterKnife.bind(this);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        dao = Data.getInstance(this);
        prefs = getSharedPreferences("com.enochtam.cisc325.makefit", MODE_PRIVATE);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetupProfileActivity.this);
                builder.setTitle("Set Profile Photo")
                        .setItems(new String[]{"Pick Photo", "Take Photo"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 1) {
                                    File path = new File(getFilesDir(), "images/");
                                    if (!path.exists()) path.mkdirs();
                                    imagePath = "image"+java.util.UUID.randomUUID().toString()+".jpg";
                                    File image = new File(path, imagePath);
                                    Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), CAPTURE_IMAGE_FILE_PROVIDER, image);
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent, 0);
                                } else {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                                }
                            }
                        });
                builder.create().show();
            }
        });


        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                prefs.edit().putString("firstname", firstName.getText().toString()).apply();
                prefs.edit().putString("lastname", lastName.getText().toString()).apply();

                if (imageUri!=null) prefs.edit().putString("imageuri",imageUri.toString()).apply();

                dao.createTestData();
                prefs.edit().putBoolean("firstrun", false).commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        setupCloseKeyboard(findViewById(R.id.profile_container));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri uri;
        switch(requestCode) {
            case 0:
                if(resultCode == -1) {
//                    uri = imageReturnedIntent.getData();
                    File path = new File(getFilesDir(), "images/");
                    if (!path.exists()) path.mkdirs();
                    File imageFile = new File(path, imagePath);
                    Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), CAPTURE_IMAGE_FILE_PROVIDER, imageFile);
                    SetupProfileActivity.this.imageUri = imageUri;
                    setProfileImage(imageUri);
                }
                break;
            case 1:
                if(resultCode == -1) {
                    uri = imageReturnedIntent.getData();
                    SetupProfileActivity.this.imageUri = uri;
                    setProfileImage(uri);
                }
                break;
        }
    }

    public void setProfileImage(Uri uri){
        try{
            Bitmap bm = MainActivity.getBitmapFromUri(this,uri);
            profileImage.setImageBitmap(MainActivity.scaleBitmap(bm));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    public void setupCloseKeyboard(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupCloseKeyboard(innerView);
            }
        }
    }


}
