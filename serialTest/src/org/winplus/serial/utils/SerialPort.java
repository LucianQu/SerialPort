/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package org.winplus.serial.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate) throws SecurityException, IOException {

		/* Check access permission */
		if(device.canRead())
		{
			Log.i("in", "文件能读");
		}
		
		
		if (!device.canRead() || !device.canWrite()) {
			try {
				Log.i("in", "现在没有权限");
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				Log.i("in", "1");
				su = Runtime.getRuntime().exec("/system/bin/su");
				Log.i("in", "2");
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				/*String cmd = "chmod 777 /dev/s3c_serial0" + "\n"
				+ "exit\n";*/
				su.getOutputStream().write(cmd.getBytes());
				Log.i("in", "3");
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					Log.i("in", "4");
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		Log.i("in", "6");
		mFd = open(device.getAbsolutePath(), baudrate);
		Log.i("in", "7");
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
		Log.i("in", "8");
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
	private native static FileDescriptor open(String path, int baudrate);
	public native void close();
	static {
		System.loadLibrary("serial_port");
	}
}
