package org.masters.breakout;

import org.masters.breakout.log.Log;
/*
*  Run an animation thread
*/

public class AnimationRunnable implements Runnable {

	private long _started;
	private Animator _animator;
	private boolean _running;
	
	private int _updateRate = 5;

	public AnimationRunnable(Animator animator) {
		_animator = animator;
	}
	
	public int getUpdateRate() {
		return _updateRate;
	}
	
	public void setUpdatehRate(int refreshRate) {
		_updateRate = refreshRate;
	}

	public void run() {
		_started = System.currentTimeMillis();

		long current = _started;
		while (_running) {
			long elapsedTime = 
				System.currentTimeMillis() - current;
			current += elapsedTime;

			_animator.update(elapsedTime);

			try {
				Thread.sleep(getUpdateRate());
			} catch (InterruptedException ex) { }
		}
	}

	public void setIsRunning(boolean running){
		_running = running;
	}

	public void stopRunning() {
		Log.info("stopRunning");
		setIsRunning(false);
	}

	public void startRunning() {
		Log.info("startRunning");
		setIsRunning(true);
	}
}
