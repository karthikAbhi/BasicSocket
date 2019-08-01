package com.nash.basicsocket;

import android.content.Context;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity {

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

        //asyncRWThread = new AsyncRWThread(getApplicationContext());

        for(int i = 0; i<10; i++){
            mBuffer[i] = (byte) i;
        }

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new SampleAsyntask().execute();

                //RWAsynTask rwAsynTask = new RWAsynTask(getApplicationContext());
                //rwAsynTask.execute(mBuffer);

                //asyncRWThread.execute();
                new createSocket().execute();

            }
        });

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connect();
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


    private void sendCommand() {
        new sendCommandATask().execute(new byte[]{0x0a});
    }

    private void connect() {

        asyncRWThread = new AsyncRWThread(getApplicationContext());
        mSocket = asyncRWThread.getSocket();
        mBufferedInputStream = asyncRWThread.getInputStream();
        mBufferedOutputStream = asyncRWThread.getOutputStream();

        if(mSocket != null || mBufferedInputStream != null || mBufferedOutputStream != null){
            Toast.makeText(getApplicationContext(),
                    "Socket Connected!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Socket Broken!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class SampleAsyntask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),
                    "onPreExecute() Called!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i<10 ; i++){
                System.out.println(i);
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(!isCancelled()){
                Toast.makeText(getApplicationContext(),
                        "Value : " + values[0].toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),
                    "Asyntask Completed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

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

            mSocket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("192.168.4.1", 23);

            try {
                mSocket.connect(socketAddress, 2000);

                if(mSocket.isConnected()){
                    mBufferedInputStream = new BufferedInputStream(mSocket.getInputStream());
                    mBufferedOutputStream = new BufferedOutputStream(mSocket.getOutputStream());
                    Log.d("Receive Buffer Size:" , String.valueOf(mSocket.getReceiveBufferSize()));
                    Log.d("Send Buffer Size:" , String.valueOf(mSocket.getSendBufferSize()));
                    Log.d("Input Shutdown:" , String.valueOf(mSocket.isInputShutdown()));
                    Log.d("Output Shutdown:" , String.valueOf(mSocket.isOutputShutdown()));
                    mSocket.sendUrgentData(10);
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
