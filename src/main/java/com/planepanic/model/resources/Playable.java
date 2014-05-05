package com.planepanic.model.resources;

import com.badlogic.gdx.utils.Disposable;

/**
 * Common interface to playable libGDX sounds / music
 */
public interface Playable extends Disposable
{
	/**
	 * Plays the sound
	 *
	 * @see com.badlogic.gdx.audio.Sound#play()
	 */
	public void play();

	/**
	 * Plays the sound and loops it
	 *
	 * @see com.badlogic.gdx.audio.Sound#setLooping(long, boolean)
	 */
	public void playLooping();

	/**
	 * Plays the sound with volume
	 *
	 * @param volume volume to play at
	 * @see com.badlogic.gdx.audio.Sound#play(float)
	 */
	public void play(float volume);

	/**
	 * Plays the sound with volume and loops it
	 *
	 * @param volume volume to play at
	 * @see com.badlogic.gdx.audio.Sound#setLooping(long, boolean)
	 */
	public void playLooping(float volume);

	/**
	 * Stops the sound
	 *
	 * @see com.badlogic.gdx.audio.Sound#stop()
	 */
	public void stop();
}
