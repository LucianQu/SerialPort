package org.winplus.serial.utils;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;

import android.os.Bundle;





import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyserialActivity extends Activity {
    /** Called when the activity is first created. */
	

	 EditText mReception;
	 FileOutputStream mOutputStream;
	 FileInputStream mInputStream;
	 SerialPort sp;
	  ReadThread  mReadThread;
    @Override
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  
    
    final Button buttonSetup = (Button)findViewById(R.id.ButtonSent);
    buttonSetup.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			mReception = (EditText) findViewById(R.id.EditTextEmission);
			  
		      try {
		    	  
			sp=new SerialPort(new File("/dev/ttyS2"),9600);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				Log.i("in", "安全问题");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
		      
		     // mReadThread = new ReadThread();
			//	mReadThread.start();
		      
		      mInputStream=(FileInputStream) sp.getInputStream();
			
		     Toast.makeText(getApplicationContext(), "open",
		    		     Toast.LENGTH_SHORT).show();
			
		}
	});
    
    
    
    final Button buttonsend= (Button)findViewById(R.id.ButtonSent1);
    buttonsend.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			
			try {
				mOutputStream=(FileOutputStream) sp.getOutputStream();
				
				mOutputStream.write(new String("send").getBytes());
				mOutputStream.write('\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
			  
		   
		      Toast.makeText(getApplicationContext(), "send",
		    		     Toast.LENGTH_SHORT).show();
			
		}
	});
    
    
    final Button buttonrec= (Button)findViewById(R.id.ButtonRec);
    buttonrec.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			
			
			mReadThread = new ReadThread();
			mReadThread.start();
			
			Toast.makeText(getApplicationContext(), "接收完了", 6000).show();
		}
	});
    
   

	
    
    }
    
    
    private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				byte[] buffer = new byte[64];
				try {
					Log.i("in", "要开始都数据了");
					size = mInputStream.available();
					Log.i("in", ""+size);
					if(mInputStream == null)
						Log.i("in", "null");
					
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
					String s = new String();
					s = reader.readLine();
					Log.i("in", s);
					sleep(3000);
					
					
				
				
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	mReception.append(new String("recive"));
 catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}


    
    void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null) {
					mReception.append(new String(buffer, 0, size));
				}
			}
		});
	}
    
    
}