package com.nash.basicsocket;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button mBtnStart, mBtnConnect, mBtnCommand, mBtnClose;
    byte[] mBuffer = new byte[10];
    Socket mSocket;
    BufferedInputStream mBufferedInputStream;
    BufferedOutputStream mBufferedOutputStream;
    AsyncRWThread asyncRWThread;
    boolean mSocketState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnConnect = findViewById(R.id.btn_connect);
        mBtnCommand = findViewById(R.id.btn_command);
        mBtnClose = findViewById(R.id.btn_close);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createSocket().execute();
            }
        });

        mBtnCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand();
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSocket();
            }
        });
    }


    /**
     * Send Data via Socket
     */
    private void sendCommand() {
        //new sendCommandATask().execute(new byte[]{0x0a});
        new advancedSendCommandATask().execute(new byte[]{0x0a});
    }

    /**
     * Asynctask for running the Network related operations - Here, sendCommand to the Printer
     */
    private class sendCommandATask extends AsyncTask<byte[], Boolean, String>{

        @Override
        protected String doInBackground(byte[]... bytes) {

            try {
                mBufferedOutputStream.write(bytes[0]);
                mBufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                mSocketState = false;
                invalidateOptionsMenu();
                return e.getMessage();
            }
            return "Success!";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("Success!"))
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Advanced Asynctask for running the Network related operations - Here, sendCommand to the Printer
     */
    private class advancedSendCommandATask extends AsyncTask<byte[], Boolean, String>{

        @Override
        protected String doInBackground(byte[]... bytes) {

            try {
                // Setting up Read timeout
                mSocket.setSoTimeout(10);
                sendata(bytes[0]);

            } catch (IOException e) {
                e.printStackTrace();
                mSocketState = false;
                invalidateOptionsMenu();
                return e.getMessage();
            }
            return "Success!";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("Success!"))
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Read-Write Thread Class
     */
    public boolean sendata(byte[] bytes) {

        boolean success = false;
        String tmp = "";

        // Write data to the Printer

        if(mSocket.isConnected() && mBufferedOutputStream != null && mBufferedInputStream != null){

            int totalLength = bytes.length;
            // write data below 500 bytes
            if (totalLength < 500) {
                try {

                    mBufferedOutputStream.write(bytes);
                    mBufferedOutputStream.flush();
                    Thread.sleep(100);

                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                tmp = readDataFromBuffer();

                if(tmp.contains("PRCDONE")) {
                    return true;
                }
                else {
                    return false;
                }
            }
            // write data above 500 bytes
            else {

                int offset = 0;
                int length = 500; //TODO: Check the wifi packet limit
                int rem = totalLength;

                do {
                    try {

                        mBufferedOutputStream.write(bytes, offset, length);
                        mBufferedOutputStream.flush();

                        tmp = readDataFromBuffer();

                    } catch (IOException e) {
                        e.printStackTrace();
                        success = false;
                    }

                    offset += length;

                    rem = rem - length;

                    if (rem < length) {
                        length = rem;
                        rem = 0;
                    }

                } while (totalLength != offset && tmp.equals("PRCDONE"));
                success = true;
            }
        }
        return success;
    }
    // Read data sent by the Printer
    private String readDataFromBuffer() {

        String readBufferData = "";

        int len = 0;

        try {

            byte[] prcdone = new byte[10];

            len = mBufferedInputStream.available();

            if(len != 0){

                //mBufferedInputStream.mark(7);
                mBufferedInputStream.read(prcdone, 0, len);

                for(byte tmp : prcdone){
                    readBufferData += (char)tmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readBufferData;
    }

    private class readerThread extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            readDataFromBuffer();
            return null;
        }
    }
    /**
     * Create a new socket
     */
    private class createSocket extends AsyncTask<Void, Boolean, String>{


        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),
                    "Attempting Socket Connection...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            //Create a socket connection
            try {
                mSocket = new Socket();
                mSocket.setReceiveBufferSize(10);
                SocketAddress socketAddress = new InetSocketAddress("192.168.4.1", 23);
                mSocket.connect(socketAddress, 2000);

                if(mSocket.isConnected()){
                    mBufferedInputStream = new BufferedInputStream(mSocket.getInputStream());
                    mBufferedOutputStream = new BufferedOutputStream(mSocket.getOutputStream());

                    Log.d("Receive Buffer Size:" , String.valueOf(mSocket.getReceiveBufferSize()));
                    Log.d("Send Buffer Size:" , String.valueOf(mSocket.getSendBufferSize()));
                    Log.d("Input Shutdown:" , String.valueOf(mSocket.isInputShutdown()));
                    Log.d("Output Shutdown:" , String.valueOf(mSocket.isOutputShutdown()));

                    /*mSocket.sendUrgentData(10);
                    if(readDataFromBuffer().contains("PRCDONE")){
                        Log.d(TAG, "PRCDONE");
                    }*/
                    publishProgress(Boolean.TRUE);
                    mSocketState = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
                mSocketState = false;
                invalidateOptionsMenu();
                publishProgress(Boolean.FALSE);
                return e.getMessage();
            }
            invalidateOptionsMenu();
            return "Socket Connection Successful...";
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            Toast.makeText(getApplicationContext(),
                    "Wifi State:"+ values[0],
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to close the Socket. If, Opened.
     */
    private void closeSocket() {
        //TODO: Check if close lock is useful.
        try {
            if(mSocket.isConnected() && !mSocket.isClosed()) {

                mSocket.close();
                mSocketState = false;
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(),
                        "Socket Closed!",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * UI Related Methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_appbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.close:
                // Close the application
                this.finish();
                return true;
            case R.id.wifi_state:
                // Current state of the Wifi
                Toast.makeText(getApplicationContext(),
                        "Wifi State...",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem menuItem = menu.getItem(0);

        if(mSocketState){
            menuItem.setIcon(R.drawable.ic_thumb_up_black_24dp);
        }
        else{
            menuItem.setIcon(R.drawable.ic_thumb_down_black_24dp);
        }
        return true;
    }
}
