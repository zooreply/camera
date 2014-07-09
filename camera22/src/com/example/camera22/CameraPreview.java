/**
 * 
 */
package com.example.camera22;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * @author keirux
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	public interface IFChangeImage{
		public void chgImage(byte[] data, Camera camera);
		public void saveImage(byte[] data);
	}
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private IFChangeImage change;
	
	/**
	 * @param context
	 */
	public CameraPreview(Context context) {
		super(context);
		
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mHolder = getHolder();
		mHolder.addCallback(this);
		
	}
	
	public void setChangeImage(IFChangeImage change){
		this.change = change;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters =  mCamera.getParameters();
		parameters.setPreviewSize(width, height);
		mCamera.startPreview();
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
				
		try{
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			
			mCamera.setPreviewCallback(prev);			
			
		}catch(IOException exception){
			mCamera.release();
			mCamera = null;
		}
	}
	
	public void takePicture(){
		mCamera.takePicture(shutter, raw, jpeg);
		Toast.makeText(getContext(), "save", 1000).show();
		try{
			mCamera.startPreview();
		}catch(Exception e){
			
		}
	}
	
	Camera.PictureCallback jpeg = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//save file
			change.saveImage(data);
		}
	};
	
	Camera.PictureCallback raw = new Camera.PictureCallback() {
			
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//raw data
		}
	};
	
	Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			//sutter sound
			
		}
	};
	
	Camera.PreviewCallback prev = new PreviewCallback() {
		
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			change.chgImage(data, camera);
		}
	};
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		this.getHolder().removeCallback(this);
		
		//event delete
		mCamera.setPreviewCallback(null);
		
		mCamera.stopPreview();				
		mCamera.release();
		mCamera = null;	
		
	}
	
}
