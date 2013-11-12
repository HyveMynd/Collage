package edu.uofu.cs4862.collage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageActivity extends Activity {
    private CollageModel model;
    private ImageLibraryFragment libraryFragment;
    private CollageFragment collageFragment;
    private LinearLayout mainLayout;
    private FrameLayout collageAreaLayout, imageListLayout;
    private boolean isHorizontal;
    private boolean isLibHidden;
    private String imagePath;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ((CollageApplication)this.getApplication()).getModel();
        libraryFragment = new ImageLibraryFragment();
        collageFragment = new CollageFragment();
        isLibHidden = false;

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(mainLayout);

        // Setup collage area layouts
        collageAreaLayout = new FrameLayout(this);
        collageAreaLayout.setBackgroundColor(Color.DKGRAY);
        collageAreaLayout.setId(42);

        // Setup list
        imageListLayout = new FrameLayout(this);
        imageListLayout.setBackgroundColor(Color.BLACK);
        imageListLayout.setId(43);

        if (getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE){
            horizontalInit();
            isHorizontal = true;
        } else {
            verticalInit();
            isHorizontal = false;
        }
        ((CollageApplication)getApplication()).loadTestData(getResources());
    }

    private int getScreenOrientation(){
        return getResources().getConfiguration().orientation;
    }

    private void horizontalInit(){
        mainLayout.addView(collageAreaLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
        mainLayout.addView(imageListLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(imageListLayout.getId(), libraryFragment);
        fragmentTransaction.add(collageAreaLayout.getId(), collageFragment);
        fragmentTransaction.commit();
    }

    private void verticalInit(){
        mainLayout.addView(collageAreaLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(collageAreaLayout.getId(), collageFragment);
        fragmentTransaction.add(collageAreaLayout.getId(), libraryFragment);
        fragmentTransaction.hide(libraryFragment);
        fragmentTransaction.commit();
        isLibHidden = true;
    }

    private void hideLibrary(){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.hide(libraryFragment);
        ft.commit();
        isLibHidden = true;
    }

    private void showLibrary(){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.show(libraryFragment);
        ft.commit();
        isLibHidden = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        if (isHorizontal){
            menu.getItem(0).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_image:
                if (isLibHidden){
                    showLibrary();
                } else {
                    hideLibrary();
                }
                return true;
            case R.id.action_take_picture:
                openCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCamera(){
        imagePath =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".jpg";
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imagePath);
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(cameraIntent, R.id.action_take_picture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.action_take_picture &&  resultCode == RESULT_OK){
            handlePhotoCapture();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handlePhotoCapture(){
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imagePath);
        Bitmap mainImage = BitmapFactory.decodeFile(imageFile.getPath());
        ImageData data = new ImageData(mainImage, imagePath.substring(0, imagePath.indexOf('.')));
        model.addImage(data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ((CollageApplication)getApplication()).restoreState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((CollageApplication)getApplication()).persist();
    }
}
