/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dtr.zxing.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dtr.zxing.activity.CaptureActivity;
import dtr.zxing.camera.CameraManager;
import dtr.zxing.utils.PlanarYUVLuminanceSource;
import yxcode.com.cn.yxdecoder.DecodeModel;
import yxcode.com.cn.yxdecoder.R;
import yxcode.com.cn.yxdecoder.base.APP;
import yxcode.com.cn.yxdecoder.utils.DateFormator;
import yxcode.com.cn.yxdecoder.utils.FileUtil;
import yxcode.com.cn.yxdecoder.utils.PreferencesUtils;

public class DecodeHandler extends Handler {
	private final String TAG = "DecodeHandler";
	private final CaptureActivity activity;
	private final MultiFormatReader multiFormatReader;
	private boolean running = true;

	private boolean flag = true;
	private long qrCost,yxCost;

	public DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}
		switch (message.what) {
		case R.id.decode:
			decode((byte[]) message.obj, message.arg1, message.arg2);
			break;
		case R.id.quit:
			running = false;
			Looper.myLooper().quit();
			break;
		}
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		Size size = activity.getCameraManager().getPreviewSize();
		if(PreferencesUtils.getInt(activity.getApplicationContext(), CameraManager.preWidth) == PreferencesUtils.DEFAULT_INT ||
				PreferencesUtils.getInt(activity.getApplicationContext(), CameraManager.preHeight) == PreferencesUtils.DEFAULT_INT	){
			PreferencesUtils.saveInt(activity.getApplicationContext(),CameraManager.preWidth,size.width);
			PreferencesUtils.saveInt(activity.getApplicationContext(),CameraManager.preHeight,size.height);
		}

		switch (APP.decodeMode){
			case QRCODE:
				decodeQRCode(data,size);
				break;
			case YXCODE:
				decodeYXCode(data,size);
				break;
			case FIRST:
				if(flag){
					decodeQRCode(data,size);
				} else {
					decodeYXCode(data,size);
				}
				flag = !flag;
				break;
			case BOTH:
				if(flag){
					decodeQRCode(data,size);
				} else {
					decodeYXCode(data,size);
				}
				flag = !flag;
				break;
		}
	}

	private PlanarYUVLuminanceSource refreshRotatedDataAndbuildSource(byte[] data,Size size){
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++)
				rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
		}

		return buildLuminanceSourceForQrCode(rotatedData, size.height, size.width);
	}


	private synchronized void decodeQRCode(byte[] data,Size size) {
		if(null != APP.qrResult || null == size){
			if(APP.decodeMode == APP.DECODE_MODE.BOTH){
				showFailed(qrCost,"camera 预览大小为空");
			}
			return;
		}
		long time = System.currentTimeMillis();
		// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
//		if(null == rotatedData){
//			rotatedData = new byte[data.length];
//		}
//		for (int y = 0; y < size.height; y++) {
//			for (int x = 0; x < size.width; x++)
//				rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
//		}


		Result rawResult = null;
		String errorMsg = "";
		PlanarYUVLuminanceSource source = refreshRotatedDataAndbuildSource(data,size);
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap);
			} catch (ReaderException re) {
				// continue
				errorMsg = re.getMessage();
			} finally {
				multiFormatReader.reset();
			}
		}
		if (rawResult != null) {
			// Don't log the barcode contents for security.
			if(rawResult.getBarcodeFormat().equals(BarcodeFormat.AZTEC)){
				String result = rawResult.getText();
				try {
					APP.qrResult = new String(result.getBytes("ISO-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				APP.qrResult = rawResult.getText();
			}

			qrCost = System.currentTimeMillis() - time;
			if(APP.decodeMode == APP.DECODE_MODE.BOTH){
				updateDebugInfo(qrCost,APP.qrResult);
				playBeepSound();
			}

			showSuccess(qrCost);
		} else {
			errorMsg="返回结果为空";
			switch (APP.decodeMode){
				case YXCODE:
					break;
				case QRCODE:
					showFailed(System.currentTimeMillis() - time,errorMsg);
					break;
				case FIRST:
					if(!APP.firstDone){
						showFailed(System.currentTimeMillis() - time,errorMsg);
					}
					break;
				case BOTH:
					if(null == APP.qrResult || null == APP.yxResult){
						showFailed(qrCost,errorMsg);
					}
					break;
			}

		}
	}


	private void playBeepSound(){
		Handler handler = activity.getHandler();
		if(null == handler){
			return;
		}
		Message.obtain(handler,R.id.play_beep).sendToTarget();
	}

	private void saveFile(byte[] data,Size size,String dir,String fileName){
		if(!APP.debug){
			return;
		}
		PlanarYUVLuminanceSource source = refreshRotatedDataAndbuildSource(data,size);

		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();

//		int width = source.getWidth();
//		int height = source.getHeight();


		Log.e("TTT","source " + source.getWidth() + " - " + source.getHeight() + " size : " + size.width + " - " + size.height + " width " +width + " height " + height );

		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);

		FileUtil.bitmapToFile(bitmap,dir,fileName);
	}

	private void deleteFile(String dir,String fileName){
		if(!APP.debug){
			return;
		}
		File f = new File(dir,fileName);
		if(f.exists()){
			f.delete();
		}
	}


	private synchronized void decodeYXCode(byte[] data,Size size){
		if(null != APP.yxResult || null == size){
			if(APP.decodeMode == APP.DECODE_MODE.BOTH){
				showFailed(yxCost,"camera预览大小为空");
			}
			return;
		}
		long time = System.currentTimeMillis();
		byte[] yxData = new byte[data.length];

		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++)
				yxData[x * size.height + size.height - y - 1] = data[x + y * size.width];
		}

		PlanarYUVLuminanceSource source = buildLuminanceSourceforYxCode(yxData, size.height, size.width);

//		Log.e("TTT","source " + source.getWidth() + " - " + source.getHeight() + " size : " + size.width + " - " + size.height);
		String fileName = "";
		Result result = null;
		if (source != null) {
			try {
					fileName = DateFormator.getFileName();
//					saveFile(data,size,FileUtil.CRASH_PATH,fileName);

					saveFile(data,size,FileUtil.CRASH_PATH,fileName);
					FileUtil.saveCrashData(fileName,yxData);
//					FileUtil.writeStrToFile(FileUtil.CRASH_PATH,fileName,DateFormator.formTime(time) + "----> width:" +source.getWidth()+"  height "+source.getHeight()+" \n "+ Arrays.toString(source.getBinaryArr()));
//				    decodeDto = Decoder.newInstance().decode(source);
//					activity.deleteLog();


				BinaryBitmap image = new BinaryBitmap(new HybridBinarizer(source));
				Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
				hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(new BarcodeFormat[] { BarcodeFormat.YX_CODE }));
				result = new MultiFormatReader().decode(image, hints);



//					FileUtil.copyFile(FileUtil.CRASH_PATH+fileName+".jpg",FileUtil.SUCCESS_PATH+fileName+".jpg");
//					FileUtil.copyFile(FileUtil.CRASH_PATH+fileName+".dat",FileUtil.SUCCESS_PATH+fileName+".dat");

					deleteFile(FileUtil.CRASH_PATH,fileName+".jpg");
					FileUtil.deleteCrashData(fileName);
					FileUtil.deleteLog(FileUtil.CRASH_PATH,fileName);

			}catch (Exception e) {
				e.printStackTrace();

				deleteFile(FileUtil.CRASH_PATH,fileName+".jpg");
				try {
					FileUtil.deleteLog(FileUtil.CRASH_PATH,fileName);
					FileUtil.deleteCrashData(fileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}

		if (null != result && !TextUtils.isEmpty(result.getText())) {
			APP.yxResult = result.getText();
			yxCost = System.currentTimeMillis() - time;
			if(APP.decodeMode == APP.DECODE_MODE.BOTH){
				updateDebugInfo(yxCost,APP.yxResult);
				playBeepSound();
			}
			saveFile(data,size,FileUtil.SUCCESS_PATH,fileName);
//			try {
//				FileUtil.saveCrashData(FileUtil.SUCCESS_PATH,fileName,yxData);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			showSuccess(yxCost);
		} else {
			String errorMsg = "null result";
//			if(!errorMsg.contains("返回码为-1")){
				saveFile(data,size,FileUtil.FAILED_PATH,fileName);
			try {
//				FileUtil.writeStrToFile(FileUtil.FAILED_PATH,fileName,DateFormator.formTime(time) + "----> width:" +source.getWidth()+"  height "+source.getHeight()+" \n "+ Arrays.toString(source.getBinaryArr()));
				FileUtil.saveCrashData(FileUtil.FAILED_PATH,fileName,yxData);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			}
			switch (APP.decodeMode){
				case YXCODE:
					showFailed(System.currentTimeMillis() - time,errorMsg);
					break;
				case QRCODE:
					break;
				case FIRST:
					if(!APP.firstDone){
						showFailed(System.currentTimeMillis() - time,errorMsg);
					}
					break;
				case BOTH:
					if(null == APP.qrResult || null == APP.yxResult){
						showFailed(yxCost,errorMsg);
					}
					break;
			}
		}
	}



	private synchronized void showSuccess(long costTime){
		Handler handler = activity.getHandler();
		if(null == handler){
			return;
		}
		switch (APP.decodeMode){
			//内码
			case QRCODE:
				Message msg = Message.obtain(handler, R.id.decode_succeeded);
				DecodeModel decodeModel = new DecodeModel();
//				decodeModel.setType(APP.DECODE_MODE.QRCODE);
//				decodeModel.setContent(APP.qrResult);
//				decodeModel.setCost(costTime);
//				msg.obj = decodeModel;

				Bundle b = new Bundle();
				b.putString(APP.TYPE, APP.DECODE_MODE.QRCODE.name());
				b.putString(APP.COST,costTime+"");
				b.putString(APP.CONTENT,APP.qrResult);
				msg.setData(b);
				msg.sendToTarget();
				break;
			case YXCODE:
				msg = Message.obtain(handler, R.id.decode_succeeded);

//				decodeModel = new DecodeModel();
//				decodeModel.setType(APP.DECODE_MODE.YXCODE);
//				decodeModel.setContent(APP.yxResult);
//				decodeModel.setCost(costTime);
//				msg.obj = decodeModel;

				b = new Bundle();

				b.putString(APP.TYPE, APP.DECODE_MODE.YXCODE.name());
				b.putString(APP.COST,costTime+"");
				b.putString(APP.CONTENT,APP.yxResult);
				msg.setData(b);
				msg.sendToTarget();
				break;
			case FIRST:
				if(APP.firstDone){
					return;
				}
				APP.firstDone = true;
				decodeModel = new DecodeModel();
				decodeModel.setType(APP.DECODE_MODE.FIRST);
				if(null != APP.qrResult){
					msg = Message.obtain(handler, R.id.decode_succeeded);
//					decodeModel.setContent(APP.qrResult);
//					decodeModel.setCost(costTime);

					b = new Bundle();
					b.putString(APP.TYPE, APP.DECODE_MODE.FIRST.name());
					b.putString(APP.COST,costTime + "");
					b.putString(APP.CONTENT,APP.qrResult);

					msg.setData(b);
					msg.sendToTarget();
				}
				if(null != APP.yxResult){
					msg = Message.obtain(handler, R.id.decode_succeeded);
//					decodeModel.setKeyInfo(APP.yxResult);
//					decodeModel.setCost(costTime);
					b = new Bundle();

					b.putString(APP.TYPE, APP.DECODE_MODE.FIRST.name());
					b.putString(APP.COST,costTime + "");
					b.putString(APP.KEYINFO,APP.yxResult);

					msg.setData(b);
					msg.sendToTarget();
				}
				break;
			case BOTH:
				if(null != APP.qrResult && null != APP.yxResult){
					msg = Message.obtain(handler, R.id.decode_succeeded);
//					decodeModel = new DecodeModel();
//					decodeModel.setType(APP.DECODE_MODE.BOTH);
//					decodeModel.setContent(APP.qrResult);
//					decodeModel.setKeyInfo(APP.yxResult);
//					decodeModel.setCost(qrCost + yxCost);
					b = new Bundle();

					b.putString(APP.TYPE, APP.DECODE_MODE.BOTH.name());
					b.putString(APP.COST,(qrCost + yxCost) + "");
					b.putString(APP.CONTENT,APP.qrResult);
					b.putString(APP.KEYINFO,APP.yxResult);


//					b.putString(APP.KEY_COST,(qrCost + yxCost) + "ms");
//					b.putString(APP.KEY_RESULT,"QR码结果:" + APP.qrResult + "\n\n央信码结果:" + APP.yxResult);
//					b.putString(APP.KEY_RESULT,"QR码结果:" + APP.qrResult + "\n\n央信码结果:" + APP.yxResult);
//					b.putString(APP.YX_CONTENT,APP.yxResult);
					msg.setData(b);
					msg.sendToTarget();
				}
				break;
		}
	}

//	private void showFailed(long costTime){
//		showFailed(costTime,"");
//	}

	private void showFailed(long costTime,String errorMsg){
		Handler handler = activity.getHandler();
		if (handler != null) {
			Message msg = Message.obtain(handler, R.id.decode_failed);
			Bundle b = msg.getData();
			b.putString(APP.CONTENT, errorMsg);
			b.putString(APP.COST, costTime+"");
			msg.setData(b);
			msg.sendToTarget();
		}
	}

	private void updateDebugInfo(long costTime,String errorMsg){
		showFailed(costTime,errorMsg);
	}

	private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
	}



	/**
	 * A factory method to build the appropriate LuminanceSource object based on
	 * the format of the preview buffers, as described by Camera.Parameters.
	 * 
	 * @param data
	 *            A preview frame.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return A PlanarYUVLuminanceSource instance.
	 */
	public PlanarYUVLuminanceSource buildLuminanceSourceForQrCode(byte[] data, int width, int height) {
		Rect rect = activity.getCropRect();
		if (rect == null) {
			return null;
		}
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
	}

	public PlanarYUVLuminanceSource buildLuminanceSourceforYxCode(byte[] data, int width, int height) {
		Rect rect = activity.getCropRect();
		if (rect == null) {
			return null;
		}
		return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
//		return new ImgForYUVByteArr(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
	}

}
