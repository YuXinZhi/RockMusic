package com.example.rockmusic.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.rockmusic.modle.Music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;

public class MusicUtils {

	public static List<Music> getMusicList(Context context) {
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver contentResolver = context.getContentResolver();
		// 查询外部存储
		/**
		 * 访问sdcard中的音频文件的URI为MediaStore.Audio.Media.EXTERNAL_CONTENT_URI，
		 * 为了使播放列表显示所以音乐文件的信息，这里需要查询sdcard里的音频文件，并把查询到的信息保存在Cursor中
		 */
		Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				// BaseColumns._ID=="_id"
				long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
				String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.TITLE));

				String singer = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.ARTIST));

				long duration = cursor.getLong(cursor.getColumnIndexOrThrow(AudioColumns.DURATION));

				String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
				String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
				String album = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.ALBUM));
				long albumid = cursor.getLong(cursor.getColumnIndex(AudioColumns.ALBUM_ID));

				if (url.endsWith(".mp3") || url.endsWith(".MP3")) {
					Music music = new Music();
					music.setTitle(title);
					music.setArtist(singer);
					music.setId(id);
					music.setUrl(url);
					music.setAlbumId(albumid);
					music.setDuration(duration);
					musicList.add(music);
				}
			}
		}

		try {
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return musicList;
		}
		return musicList;
	}
}
