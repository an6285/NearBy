package com.example.ashish.maps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ashish on 02-09-2017.
 */

public class ConnectionDetection {
    Context context;
    public ConnectionDetection(Context context1)
    {
        this.context=context1;
    }
    public boolean isConnectedToInternet()
    {
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo[] ni=cm.getAllNetworkInfo();
            if(ni!=null){
                for(int i=0;i<ni.length;i++)
                {
                    if(ni[i].getState()==NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
