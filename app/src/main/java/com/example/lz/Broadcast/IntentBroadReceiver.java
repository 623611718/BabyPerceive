package com.example.lz.Broadcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.lz.babyperceive.MainActivity;

public class IntentBroadReceiver extends BroadcastReceiver{

	private String TAG = "BroadcastReceiver";
	private Context mcontext;

	public IntentBroadReceiver(Context context) {
		mcontext =context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG, "BroadcastReceiver-intent" + intent.getAction());
		// TODO Auto-generated method stub
		//wifi连接上与否
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

			NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
				Log.i(TAG, "wifi断开");
				Dialog(context);
			} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				//获取当前wifi名称
				Log.i(TAG, "连接到WIFI " + wifiInfo.getSSID());
				Toast.makeText(context,"当前连接的是WIFI",Toast.LENGTH_LONG).show();
			}
		}

	}
	private void Dialog(final Context context){
		AlertDialog.Builder builder =new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("当前没有连接WIFI!!!");
		builder.setCancelable(false);//设置不可取消
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//android.os.Process.killProcess(android.os.Process.myPid());
				((Activity)context).finish();
			}
			});
	
		AlertDialog dialog = builder.create();
		dialog.show();
			

	}

}
