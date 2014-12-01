package com.ruitu.btchelper.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class RingtoneUtils {
	private Context mContext;

	public RingtoneUtils(Context context) {
		this.mContext = context;
	}

	public Ringtone getDefaultRingtone(int type) {

		return RingtoneManager.getRingtone(mContext,
				RingtoneManager.getActualDefaultRingtoneUri(mContext, type));

	}

	public Uri getDefaultRingtoneUri(int type) {

		return RingtoneManager.getActualDefaultRingtoneUri(mContext, type);

	}

	public List<Uri> getRingtoneUriList(int type) {

		List<Uri> resArr = new ArrayList<Uri>();

		RingtoneManager manager = new RingtoneManager(mContext);

		manager.setType(type);

		Cursor cursor = manager.getCursor();

		int count = cursor.getCount();

		for (int i = 0; i < count; i++) {

			resArr.add(manager.getRingtoneUri(i));
		}
		return resArr;

	}

	public Ringtone getRingtone(int type, int pos) {

		RingtoneManager manager = new RingtoneManager(mContext);

		manager.setType(type);

		return manager.getRingtone(pos);

	}

	public List<String> getRingtoneTitleList(int type) {

		List<String> resArr = new ArrayList<String>();

		RingtoneManager manager = new RingtoneManager(mContext);

		manager.setType(type);

		Cursor cursor = manager.getCursor();

		if (cursor.moveToFirst()) {

			do {

				resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));

			} while (cursor.moveToNext());

		}

		return resArr;

	}


	public Uri getRingtoneUriPath(int type, int pos) {

		RingtoneManager manager = new RingtoneManager(mContext);

		manager.setType(type);

		Uri uri = manager.getRingtoneUri(pos);

		return uri;

	}

	public Ringtone getRingtoneByUriPath(int type, String uriPath) {

		RingtoneManager manager = new RingtoneManager(mContext);

		manager.setType(type);

		Uri uri = Uri.parse(uriPath);

		return manager.getRingtone(mContext, uri);

	}
}
