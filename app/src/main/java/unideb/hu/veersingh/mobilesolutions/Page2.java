package unideb.hu.veersingh.mobilesolutions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult;
import edmt.dev.edmtdevcognitivevision.Contract.Caption;
import edmt.dev.edmtdevcognitivevision.Rest.VisionServiceException;
import edmt.dev.edmtdevcognitivevision.VisionServiceClient;
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient;

public class Page2 extends AppCompatActivity {

    //Pick image from gallery
    ImageView chooseImage;
    Button btnChooseImage;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //Image recognition
    Button btnProcess;
    TextView txtResult;
    private final String API_KEY = "b3705ed903fe4a2d9512f4aceb5a8f04";
    private final String API_LINK = "https://veersinghunideb.cognitiveservices.azure.com/vision/v1.0";
    VisionServiceClient visionServiceClient = new VisionServiceRestClient(API_KEY, API_LINK);

    //Not working?
    private Button btnNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Locks UI in portrait mode

        chooseImage = findViewById(R.id.pick_image_gallary);
        btnChooseImage = findViewById(R.id.btn_choose_image);

        //handle button click
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
            }
        });

        //not working button
        btnNotWorking = (Button)findViewById(R.id.btn_not_working);
        btnNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_NotWorking();
            }
        });
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else{
                    //permission was denied
                    Toast.makeText(this,"Permission denied...!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //handle result of picked image
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode ==IMAGE_PICK_CODE){
            //show chosen image
            //chooseImage.setImageURI(data.getData());
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                final Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                chooseImage.setImageBitmap(bmp);

                //////////////////// Image Recognition \\\\\\\\\\\\\\\\\\\\
                btnProcess = findViewById(R.id.btn_process);
                txtResult =  findViewById(R.id.txt_result);

                btnProcess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Convert bitmap to byte array
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                        //Use async task to request API
                        AsyncTask<InputStream,String,String> visionTask = new AsyncTask<InputStream, String, String>() {
                            ProgressDialog progressDialog = new ProgressDialog(Page2.this);
                            @Override
                            protected void onPreExecute() {
                                progressDialog.show();
                            }

                            @Override
                            protected String doInBackground(InputStream... inputStreams) {
                                try{
                                    publishProgress("Recognizing...");
                                    String[] features = {"Description"}; // Get description from API, return result
                                    String[] details = {};

                                    AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0],features,details);

                                    String jsonResult = new Gson().toJson(result);
                                    return jsonResult;

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (VisionServiceException e) {
                                    e.printStackTrace();
                                }
                                return "";
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                if(TextUtils.isEmpty(s)){
                                    Toast.makeText(Page2.this,"Sorry I cant understand this image... Blame - Veer Singh! ", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                                else {
                                    progressDialog.dismiss();

                                    AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                                    StringBuilder result_text = new StringBuilder();
                                    for (Caption caption : result.description.captions)
                                        result_text.append(caption.text);
                                    txtResult.setText(result_text.toString());
                                }
                            }

                            @Override
                            protected void onProgressUpdate(String... values) {
                                progressDialog.setMessage(values[0]);
                            }
                        };
                        visionTask.execute(inputStream); //Runs the task
                    }
                });
                //////////////////// Image Recognition ends \\\\\\\\\\\\\\\\\\\
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void open_NotWorking(){
        Intent openNotWorking = new Intent(this, NotWorking.class);
        startActivity(openNotWorking);
    }
}