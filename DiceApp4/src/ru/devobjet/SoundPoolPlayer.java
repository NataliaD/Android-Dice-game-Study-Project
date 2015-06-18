package ru.devobjet;

import java.util.HashMap;

import ru.devprojet.diceapp4.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundPoolPlayer {

	private SoundPool mShortPlayer= null;
	private HashMap<Integer,Integer> mSounds = new HashMap<Integer,Integer>();

	public SoundPoolPlayer(Context pContext)
	{
		// setup Soundpool
		this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

		// load some sound effect files
		mSounds.put(R.raw.dice_throw1, this.mShortPlayer.load(pContext, R.raw.dice_throw1, 1));
		mSounds.put(R.raw.dice_throw2, this.mShortPlayer.load(pContext, R.raw.dice_throw2, 1));
		mSounds.put(R.raw.argh, this.mShortPlayer.load(pContext, R.raw.argh, 1));
		mSounds.put(R.raw.cheer, this.mShortPlayer.load(pContext, R.raw.cheer, 1));
		mSounds.put(R.raw.scribble, this.mShortPlayer.load(pContext, R.raw.scribble, 1));
		mSounds.put(R.raw.impressed, this.mShortPlayer.load(pContext, R.raw.impressed, 1));
		
	}

	public void playShortResource(int piResource) {
		int iSoundId = mSounds.get(piResource);
		this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);
	}

	// Cleanup
	public void release() {
		// Cleanup
		this.mShortPlayer.release();
		this.mShortPlayer = null;
	}
}
