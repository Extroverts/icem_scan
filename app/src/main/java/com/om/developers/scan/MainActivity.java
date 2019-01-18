package com.om.developers.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 7;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";

    boolean isEmail = false;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_main );
        initViews();

    }

    private void initViews ( ) {
        txtBarcodeValue = findViewById( R.id.txtBarcodeValue );
        surfaceView = findViewById( R.id.surfaceView );

    }

    private void initialiseDetectorsAndSources ( ) {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder( this )
                .setBarcodeFormats( Barcode.ALL_FORMATS )
                .build();

        cameraSource = new CameraSource.Builder( this, barcodeDetector )
                .setRequestedPreviewSize( 1920, 1080 )
                .setAutoFocusEnabled( true ) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback( new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated (SurfaceHolder holder) {
                try
                    {
                        if ( ContextCompat.checkSelfPermission( getApplicationContext(),
                                Manifest.permission.CAMERA )
                             != PackageManager.PERMISSION_GRANTED )
                            {

                                // Permission is not granted
                                // Should we show an explanation?
                                if ( ActivityCompat.shouldShowRequestPermissionRationale( MainActivity.this,
                                        Manifest.permission.CAMERA ) )
                                    {
                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.
                                    } else
                                    {
                                        // No explanation needed; request the permission
                                        ActivityCompat.requestPermissions( MainActivity.this,
                                                new String[]{ Manifest.permission.CAMERA },
                                                MY_PERMISSIONS_REQUEST_READ_CONTACTS );

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                            } else
                            {
                                // Permission has already been granted
                            }
                        if ( ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED )
                            {
                                cameraSource.start( surfaceView.getHolder() );
                            } else
                            {
                                ActivityCompat.requestPermissions( MainActivity.this, new
                                        String[]{ Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION );
                            }

                    } catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
            }

            @Override
            public void surfaceChanged (SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed (SurfaceHolder holder) {
                cameraSource.stop();
            }
        } );
        barcodeDetector.setProcessor( new Detector.Processor <Barcode>() {
            @Override
            public void release ( ) {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections (Detector.Detections <Barcode> detections) {
                final SparseArray <Barcode> barcodes = detections.getDetectedItems();

                if ( barcodes.size() != 0 )
                    {
                        txtBarcodeValue.post( new Runnable() {
                            @Override
                            public void run ( ) {
                                if ( barcodes.valueAt( 0 ).email != null )
                                    {
                                        txtBarcodeValue.removeCallbacks( null );
                                        intentData = barcodes.valueAt( 0 ).email.address;
                                        txtBarcodeValue.setText( intentData );
                                        isEmail = true;
                                    } else
                                    {
                                        isEmail = false;
                                        intentData = barcodes.valueAt( 0 ).displayValue;
                                        txtBarcodeValue.setText( intentData );
                                        Intent news = new Intent( MainActivity.this, ICEM.class );
                                        news.putExtra( "datas", intentData );
                                        startActivity( news );
                                        finish();
                                    }
                            }
                        } );

                    }
            }
        } );
    }

    @Override
    protected void onPause ( ) {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume ( ) {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}