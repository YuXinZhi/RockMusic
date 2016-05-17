package com.example.rockmusic;

import java.util.ArrayList;
import java.util.List;

import com.example.rockmusic.fragment.AllMusicFragment;
import com.example.rockmusic.fragment.BaseFragment;
import com.example.rockmusic.fragment.FavoriteMusicFragment;
import com.example.rockmusic.fragment.InternetMusicFragment;
import com.example.rockmusic.service.MusicPlayService;
import com.example.rockmusic.service.MusicPlayService.MusicServiceBinder;
import com.example.rockmusic.service.MusicPlayService.StateChangedListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener, StateChangedListener {

	private MusicPlayService mMusicPlayService;
	private ServiceConnection mServiceConnection;
	// 导航栏
	private ImageView mAppIconImageView, mLocalImageView, mFavouriteImageView, mInternetImageView;
	private List<ImageView> mNavigatorViews;
	private List<Fragment> mFragments;

	private ViewPager mViewPager;

	// 播放控制按钮
	private ImageButton mPlayButton, mNextButton, mPreviousButton, mFavoriteButton;

	// 歌曲名
	private TextView titleTextView;
	// 专辑封面
	private ImageView mArtImageView;

	private RelativeLayout mRootLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 根布局
		if (mRootLayout == null) {
			mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
		}
		// 初始化ActionBar
		initActionBar();
		// 初始化控制按钮
		initControlButtons();
		// 初始化PagerView各个页面
		initPages();
		// 绑定服务
		bindToService();
		// 启动服务
		startService();
	}

	@Override
	protected void onDestroy() {
		// 解除与服务的绑定
		unbindService(mServiceConnection);
		super.onDestroy();
	}

	private void startService() {
		final Intent intent = new Intent();
		intent.setClass(MainActivity.this, MusicPlayService.class);
		startService();
	}

	private void bindToService() {
		mServiceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			// 服务连接建立时调用
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				MusicServiceBinder binder = (MusicServiceBinder) service;
				mMusicPlayService = binder.getService();
				mMusicPlayService.setActivityCallback(MainActivity.this);
				// 更新
				initPagerView();
				onPlayStateChanged();
			}
		};
	}

	private void initPagerView() {

	}

	private void initPages() {
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new AllMusicFragment());
		mFragments.add(new FavoriteMusicFragment());
		mFragments.add(new InternetMusicFragment());

	}

	private void initControlButtons() {
		mPlayButton = (ImageButton) findViewById(R.id.btn_play_local);
		mNextButton = (ImageButton) findViewById(R.id.btn_next_local);
		mPreviousButton = (ImageButton) findViewById(R.id.btn_pre_local);
		mFavoriteButton = (ImageButton) findViewById(R.id.btn_praised);

		mPlayButton.setOnClickListener(this);
		mPreviousButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		// 专辑封面
		mArtImageView = (ImageView) findViewById(R.id.iv_art_bottom);
		// 歌名
		titleTextView = (TextView) findViewById(R.id.title);

		mFavoriteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mMusicPlayService != null) {
					if (mMusicPlayService.onPraisedBtnPressed()) {
						mFavoriteButton.setImageResource(R.drawable.btn_loved_prs);
					} else {
						mFavoriteButton.setImageResource(R.drawable.btn_love_prs);
					}
				}
				// 收藏歌曲
				((BaseFragment) mFragments.get(1)).onPraisedPressed();
			}
		});
	}

	private void initActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.action_bar);
		mAppIconImageView = (ImageView) findViewById(R.id.app_icon);
		mFavouriteImageView = (ImageView) findViewById(R.id.favour);
		mLocalImageView = (ImageView) findViewById(R.id.local);
		mInternetImageView = (ImageView) findViewById(R.id.internet);

		// 设置Actionbar栏图标的监听事件
		mAppIconImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toogleDrawer();
			}
		});

		mInternetImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(2, true);
			}
		});

		mFavouriteImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1, true);
			}
		});

		mLocalImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0, true);
			}
		});

		mNavigatorViews = new ArrayList<ImageView>();
		mNavigatorViews.add(mLocalImageView);
		mNavigatorViews.add(mFavouriteImageView);
		mNavigatorViews.add(mInternetImageView);
	}

	protected void toogleDrawer() {

	}

	// 状态改变时调用
	@Override
	public void onPlayStateChanged() {
		updateControlButtonBackground();
		updateArtImage(mArtImageView);
		updateTitle(mMusicPlayService.getCurrentTitle());
		updateFavoriteImage();
	}

	@Override
	public void onClick(View v) {
		int btnId = v.getId();
		switch (btnId) {
		// 播放
		case R.id.btn_play_local:
			// 判断是否正在播放
			if (mMusicPlayService.getIsPlaying()) {
				mMusicPlayService.pausePlayer();
			} else {
				mMusicPlayService.resumePlayer();
			}
			break;
		// 下一曲
		case R.id.btn_next_local:
			mMusicPlayService.playNext();
			break;
		// 上一曲
		case R.id.btn_pre_local:
			mMusicPlayService.playPrevious();
			break;
		default:
			break;
		}
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
	}

	// 更新播放按钮
	public void updateControlButtonBackground() {
		if (mMusicPlayService.getIsPlaying()) {
			mPlayButton.setImageResource(R.drawable.pause);
		} else {
			mPlayButton.setImageResource(R.drawable.play);
		}
	}

	// 更新专辑封面
	public void updateArtImage(ImageView imageView) {
		if (mMusicPlayService.getPlayList() != null) {
			ImageLoader.getInstance().displayImage(mMusicPlayService.getCurrentAlbumUri().toString(), imageView);
		}
	}

	// 更新歌曲名
	public void updateTitle(String title) {
		titleTextView.setText(title);
	}

	// 更新收藏按钮
	public void updateFavoriteImage() {
		if (mMusicPlayService != null && mMusicPlayService.checkIfPraised()) {
			mFavoriteButton.setImageResource(R.drawable.btn_loved_prs);
		} else {
			mFavoriteButton.setImageResource(R.drawable.btn_love_prs);
		}
	}
}
