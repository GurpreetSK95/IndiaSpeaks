package gurpreetsk.me.indiaspeaks;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import gurpreetsk.me.indiaspeaks.Models.Feed;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Feed");      //TODO: use username/email here
    Bitmap myBitmap = null;

    private String TAG = "MainActivity";

    EditText complaintET;
    Button btn;
//    String pincode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


        btn = (Button) findViewById(R.id.picClickedBtn);
        complaintET = (EditText) findViewById(R.id.feedET);

//        pincode = getIntent().getExtras().getString("PINCODE");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void picClicked(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 3);
        else {
            //take photo
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri fileUri = getOutputMediaFileUri(1);          // create a file to save the image
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);        // set the image file name
            startActivityForResult(cameraIntent, 1);
            showImageInImageView();
            btn.setText("");
        }
    }

    public void sendToFireBase(View view) {

//        picClicked(view);
        String Complaint = complaintET.getText().toString();
        String date_time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        if (!Complaint.equals("") && myBitmap != null && !AuthActivity.pincode.equals("")) {

            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 30, bYtE);
            Bitmap bMapScaled = Bitmap.createScaledBitmap(myBitmap, 10, 12, true);
            bMapScaled.recycle();
            byte[] byteArray = bYtE.toByteArray();
          //  String bitmap = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String bitmap = "a9";
            String name = "Sh. Raghuvinder Shokeen";
            String email = "mla.nangloijat-dl@gov.in";
            String link = "RaghuvinderShokeen";
            String pincode = "110041";

            Feed feedPush = new Feed(Complaint, bitmap, date_time, pincode, name, email, link);
            myRef.push().setValue(feedPush);
            btn.setText("Click!");
            Toast.makeText(this, "Complaint successfully Sent", Toast.LENGTH_SHORT).show();

        } else
            Snackbar.make(view, "Please Enter all data", Snackbar.LENGTH_LONG).show();

        complaintET.setText("");

    }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
        return Uri.fromFile(getOutputMediaFile(type));  // FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));   // Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaFile;
        if (type == 1) {
            mediaFile = new File(Environment.getExternalStorageDirectory().getPath().concat("/Download/") + File.separator + "IndiaSpeaks_1" + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
                showImageInImageView();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImageInImageView() {
        File imgFile = new File(Environment.getExternalStorageDirectory().getPath().concat("/Download/IndiaSpeaks_1.jpg"));
//        Date lastModDate = new Date(imgFile.lastModified());
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.clickedImage);
            myImage.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please provide storage permission.", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please provide camera access.", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
