package me.winds.album.tools;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommonTools {

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 吐司提示
	 */
	public static Toast mToast;
	public static void showTips(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		}

		mToast.setText(text);
		mToast.show();
	}

	/**
	 * 判断当前网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前是否是WiFi连接
	 * @param context
	 * @return
     */
	public static boolean isWiFiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断服务是否运行
	 * 
	 * @param context
	 * @param clazz
	 *            要判断的服务的class
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class<? extends Service> clazz) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo> services = manager.getRunningServices(100);
		for (int i = 0; i < services.size(); i++) {
			String className = services.get(i).service.getClassName();
			if (className.equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取设备编号
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String device_id = getPreference(context, "device_id", "");
		if(TextUtils.isEmpty(device_id)) {
			device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			savePreference(context, "device_id", device_id);
		}
		return device_id;
	}
	
	/**
	 * 保存Preference信息
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void savePreference(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).apply();
	}
	
	/**
	 * 获取Preference信息
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getPreference(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	
	/**
	 * 获得格式化后的当前时间
	 * @param time
	 * @return
	 */
	public static String getFormatTime(long time) {
		if (sdf == null) {
			sdf = new SimpleDateFormat();
		}
		sdf.applyPattern("yyyy-MM-dd");
		String format = sdf.format(time);
		return format;
	}

	/**
	 * 获得格式化后的当前时间
	 * @param time
	 * @return
	 */
	public static SimpleDateFormat sdf;
	public static String getFormatTime(String format) {
		if (sdf == null) {
			sdf = new SimpleDateFormat();
		}
		sdf.applyPattern(format);
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * 格式化指定日期指定格式的时间
	 * @param pattern
	 * @param time
     * @return
     */
	public static String getConvertTime(String pattern, Long time) {
		if (sdf == null) {
			sdf = new SimpleDateFormat();
		}
		sdf.applyPattern(pattern);
		return sdf.format(time);
	}
	
	/**
	 * 把已经格式化好的时间转换成其他格式的时间
	 * @param oldPattern
	 * @param newPattern
	 * @param time
	 * @return
	 */
	public static String getConvertTime(String oldPattern, String newPattern, String time) {
		if (sdf == null) {
			sdf = new SimpleDateFormat();
		}
		try {
			sdf.applyPattern(oldPattern);
			Date date = sdf.parse(time);
			sdf.applyPattern(newPattern);
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * String类型时间转长整型
	 * @param pattern
	 * @param time
     * @return
     */
	public static long getLongTime(String pattern, String time){
		if (sdf == null) {
			sdf = new SimpleDateFormat();
		}
		sdf.applyPattern(pattern);
		try {
			Date date = sdf.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 旋转Bitmap
	 * 
	 * @param b
	 * @param rotateDegree
	 * @return
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
		Matrix matrix = new Matrix();
		matrix.postRotate((float) rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度和高度，单位为px
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		return new Point(w_screen, h_screen);
	}

	/**
	 * 获取屏幕长宽比
	 * 
	 * @param context
	 * @return
	 */
	public static float getScreenRate(Context context) {
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H / W);
	}
}
