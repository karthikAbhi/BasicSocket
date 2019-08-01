package com.nash.basicsocket;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class RWAsynTask extends AsyncTask<byte[], Void, Boolean> {

    private Context mContext;

    public RWAsynTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(byte[]... bytes) {
        try{
            for(int i = 0; i<bytes[0].length; i++){
                System.out.println(bytes[0][i]);
                Thread.sleep(1000);
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            Toast.makeText(mContext,
                    "Success...",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Toast.makeText(mContext,
                    "Failed...",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
