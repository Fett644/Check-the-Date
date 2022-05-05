package com.example.checkthedate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.IOException;


public class AddItemActivity extends AppCompatActivity {
    // Declaration of global variables
    // Linked barcodeDetector API from google play services which is being used
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Declaration of local variables
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        initialiseDetectorsAndSources();

        TextView txtNameAddWelcome = (TextView) findViewById(R.id.textView_add_welcome);
        TextView goBack = (TextView) findViewById(R.id.textViewScannerBack);
        Button btn_manually_add = (Button) findViewById(R.id.btn_manually_add);
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        txtNameAddWelcome.setText("Welcome to the barcode scanner " + loginName +
                ", scan items to add to household inventory");


        barcodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            /* Detects when the barcode has been detected and sends it through to the manually add
            activity */
            @Override
            public void afterTextChanged(Editable editable) {
                Intent intent = new Intent(AddItemActivity.this,
                        ManuallyAddItem.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("BarcodeData", barcodeData);
                startActivity(intent);
            }
        });

        // Allows user to skip barcode scanner if they want to manually add or don't have a barcode
        btn_manually_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this,
                        ManuallyAddItem.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("BarcodeData", barcodeData);
                startActivity(intent);
            }
        });

        // Allows user to go back to previous activity
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", "");
                startActivity(intent);
                finish();
            }
        });
    }

    // Initialises the camera and allows it to be used to get barcode
    private void initialiseDetectorsAndSources() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddItemActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddItemActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(),
                // "To prevent memory leaks barcode scanner has been stopped",
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes;
                barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        }
                    });

                }
            }
        });
    }

}