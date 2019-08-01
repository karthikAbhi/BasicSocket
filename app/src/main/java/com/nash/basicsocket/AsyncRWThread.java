package com.nash.basicsocket;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class AsyncRWThread extends AsyncTask<Void, Void, String> {

    Context mContext;
    Socket mSocket;
    BufferedInputStream in;
    BufferedOutputStream out;

    public AsyncRWThread(Context context) {
        mContext = context;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public BufferedInputStream getInputStream() {
        return in;
    }

    public BufferedOutputStream getOutputStream() {
        return out;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mContext,
                "Attempting Socket Connection...",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Void... bytes) {

        //Create a socket connection

        mSocket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress("192.168.4.1", 23);

        try {
            mSocket.connect(socketAddress, 2000);

            if(mSocket.isConnected()){
                in = new BufferedInputStream(mSocket.getInputStream());
                out = new BufferedOutputStream(mSocket.getOutputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Socket Connection Successful...";
    }

    @Override
    protected void onPostExecute(String s) {

        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();


//        if(!aBoolean){
//            Toast.makeText(mContext,
//                    "Socket Error...",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(mContext,
//                    "Socket connection successful...",
//                    Toast.LENGTH_SHORT).show();
//        }
    }
}
