package com.example.rockmusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayService extends Service {

	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MusicServiceBinder extends Binder {
		public MusicPlayService getService() {
			return MusicPlayService.this;
		}
	}
	
	public boolean onPraisedBtnPressed() {
		return false;
	}

	public interface StateChangedListener {
		void onPlayStateChanged();
	}

	public boolean getIsPlaying() {
		return false;
	}

	public Object getPlayList() {
		return null;
	}

	public Object getCurrentAlbumUri() {
		return null;
	}

	public boolean checkIfPraised() {
		return false;
	}

	public void pausePlayer() {
		
	}

	public void resumePlayer() {
		
	}

	public void playNext() {
		
	}

	public void playPrevious() {
		
	}

	public void setActivityCallback(Context context) {
		
	}

	public String getCurrentTitle() {
		return null;
	}

}
