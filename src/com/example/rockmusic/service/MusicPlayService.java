package com.example.rockmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MusicPlayService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public boolean onPraisedBtnPressed() {
		return false;
	}

	public interface StateChangedListener {
		void onPlayStateChanged();
	}

	public boolean getIsPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getPlayList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCurrentAlbumUri() {
		return null;
	}

	public boolean checkIfPraised() {
		// TODO Auto-generated method stub
		return false;
	}

	public void pausePlayer() {
		// TODO Auto-generated method stub
		
	}

	public void resumePlayer() {
		// TODO Auto-generated method stub
		
	}

	public void playNext() {
		// TODO Auto-generated method stub
		
	}

	public void playPrevious() {
		// TODO Auto-generated method stub
		
	}

}
