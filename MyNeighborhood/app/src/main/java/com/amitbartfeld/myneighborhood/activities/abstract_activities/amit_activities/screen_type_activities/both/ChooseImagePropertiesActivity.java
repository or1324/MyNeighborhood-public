package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.both;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenPropertiesActivity;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerStorage;
import com.amitbartfeld.myneighborhood.properties.type.CloudStorageProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class ChooseImagePropertiesActivity extends MainScreenPropertiesActivity {
    protected Bitmap image;
    protected ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            try {
                if (result != null) {
                    InputStream inputStream = getContentResolver().openInputStream(result);
                    ExifInterface ei = new ExifInterface(inputStream);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    inputStream = getContentResolver().openInputStream(result);
                    image = BitmapFactory.decodeStream(inputStream);
                    rotateImageByOrientationToFixDefaultRotation(image, orientation);
                    showImageChosen();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showErrorMessage(MessagesTexts.imageError);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.properties_button).setVisibility(View.VISIBLE);
        findViewById(R.id.properties_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    protected void chooseImage() {
        activityResultLauncher.launch("image/*");
    }

    //shows to the user that the image was chosen successfully.
    protected void showImageChosen() {
        //TODO show image chosen
        findViewById(R.id.image_layout).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.chosen_image)).setImageBitmap(image);
    }

    protected abstract void uploadToFirestore(String downloadURL);

    @Override
    public void upload() {
        //upload image
        startLoading();
        CloudOperations.uploadToCloudStorage(new CloudStorageProperties(screenType, image), new InternetErrorUploadListenerStorage(this) {
            @Override
            public void onUploadFinished(String downloadURL) {
                uploadToFirestore(downloadURL);
            }
        });
    }

    private void rotateImageByOrientationToFixDefaultRotation(Bitmap bitmap, int orientation) {
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                image = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                image = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                image = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                image = bitmap;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
