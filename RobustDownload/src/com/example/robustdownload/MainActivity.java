package com.example.robustdownload;




import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private int current=0;
	public Button button;
	public EditText URLField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button=(Button) findViewById(R.id.button1);
		URLField=(EditText) findViewById(R.id.editText1);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new Thread(){
					public void run(){
						try {
							String URLValue=URLField.getText().toString();

							downloadFromURL(URLValue);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.err.println("Failed to download");
						}

					}}.start();
			}
			
	    });
			
		
	
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public void downloadFromURL(String URLValue) throws IOException{
		URL url = new URL(URLValue);
		String fileName="file.pdf";
        File file = new File("storage/sdcard0/Android/data/com.example.RobustDownload/",fileName);

        long startTime = System.currentTimeMillis();
        System.out.println("Download Starting");
        System.out.println("Download url:" + url);
        System.out.println("Downloaded:" +  getFilesDir() + File.separator + fileName);
        System.out.println("1");
        int x=0;
       while(isOnline()==false){
        	try {
				Thread.sleep(6000); 
				x++;
				System.out.println("Sleep "+ x);
				if(x==1){
			        new Thread(){ 
			        	public void run(){
					        Looper.prepare();

					Context context = getApplicationContext();
					CharSequence text = "Network Connection is down: Dowload will resume when it's back up";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					Looper.loop();
					Looper.myLooper().quit();
			        }
			        }.start();

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Could not sleep");
			}
        }
        URLConnection ucon = url.openConnection();
        System.out.println("2");
        InputStream is = ucon.getInputStream();
        System.out.println("3");
        BufferedInputStream bis = new BufferedInputStream(is);
        System.out.println("4");
        ByteArrayBuffer baf = new ByteArrayBuffer(1000);
        System.out.println("5");
        
       
        try{
        while ((current = bis.read()) != -1) {
        	
        	
           baf.append((byte) current);
        	
        
         
        }
        }
        catch(SocketException E){
        	downloadFromURL(URLValue);
        	
        }
        catch(IOException I){
        	downloadFromURL(URLValue);

        
        }
	
        System.out.println("6");

        
        FileOutputStream fos = new FileOutputStream(file);
        System.out.println("7");
        fos.write(baf.toByteArray());
        System.out.println("8");
        fos.close();
        Log.d("DownloadManager", "download ready in"
                        + ((System.currentTimeMillis() - startTime) / 1000)
                        + " sec");
		
	}

}
