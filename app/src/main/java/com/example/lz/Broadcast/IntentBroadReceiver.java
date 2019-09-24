package com.example.lz.Broadcast;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.lz.babyperceive.MainActivity;

public class IntentBroadReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager manager =(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.i("dsa","IntentBroadReceiver networkInfo  "+wifiNetworkInfo);
		if( wifiNetworkInfo!=null&&wifiNetworkInfo.isAvailable()){
			Toast.makeText(context,"当前连接的是WIFI",Toast.LENGTH_LONG).show();
		}else{
			Dialog(context);
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
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			});
	
		AlertDialog dialog = builder.create();
		dialog.show();
			
			
	}

}
