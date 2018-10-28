package net.mgsx.overtime.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import net.mgsx.overtime.OverTime;

public class AudioEngine 
{
	private static AudioEngine i;
	
	public static AudioEngine i(){
		return i == null ? i = new AudioEngine() : i;
	}
	
	private static float SFX_GENERAL = .2f;
	private static float DRONE_GENERAL = .5f;
	private static float AUDIO_GENERAL = 1f;
	
	private Sound sndDrone;
	private long drone1, drone2;
	private float audioTime;
	
	private float droneBase, droneCurrent;
	private float droneTransitionSpeed = 2f;

	private final static String [] sfxPath = {
			"bonus", // 
			"bonus2", 
			"count2", 
			"counter", 
			"freeze", 
			"glitch", 
			"glitch2", 
			"loose", 
			"shift", 
			"swap", 
			"timedown", 
			"timeup", 
			"win"
	};
	
	private Array<Sound> sfx = new Array<Sound>();
	
	private boolean randomDrone;
	private float droneTimeout;
	
	public AudioEngine() 
	{
		for(String path : sfxPath){
			sfx.add(Gdx.audio.newSound(Gdx.files.internal("sfx/" + path + ".wav")));
		}
		
		sndDrone = Gdx.audio.newSound(Gdx.files.internal("audio/looped.wav"));
		drone1 = sndDrone.loop(droneVolume(), .5f, .5f);
		drone2 = sndDrone.loop(droneVolume(), .5f, -.5f);
		
		droneCurrent = droneBase = .25f;
	}
	
	private float droneVolume(){
		return AUDIO_GENERAL * DRONE_GENERAL;
	}
	private float sfxVolume(){
		return AUDIO_GENERAL * SFX_GENERAL;
	}
	
	public void update(float delta){
		
		// XXX DEBUG
		if(OverTime.DEBUG){
			for(int i=0 ; i<12 ; i++){
				if(Gdx.input.isKeyJustPressed(Input.Keys.F1 + i)){
					sfx.get(i).play(.2f); // TODO SFX general volume
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
				sfx.get(12).play(.2f); // TODO SFX general volume
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
				droneBase = MathUtils.random(.7f, 1.2f);
			}
		}
		// XXX DEBUG
		
		audioTime += delta;
		
		if(randomDrone){
			droneTimeout += delta;
			if(droneTimeout > 5){
				droneTimeout = 0;
				droneBase = MathUtils.random(.7f, 1.2f);
			}
		}
		
		droneCurrent = MathUtils.lerp(droneCurrent, droneBase, delta * droneTransitionSpeed);
		
		sndDrone.setPitch(drone1, MathUtils.lerp(droneCurrent, droneCurrent * 1.03f, MathUtils.sinDeg(audioTime * 100) * .5f + .5f));
		sndDrone.setPitch(drone2, MathUtils.lerp(droneCurrent, droneCurrent * .99f, MathUtils.sinDeg(audioTime * 114 + 149) * .5f + .5f));
	
		sndDrone.setVolume(drone1, droneVolume());
		sndDrone.setVolume(drone2, droneVolume());
	}

	public void start() 
	{
		randomDrone = true;
		droneCurrent = .25f;
		droneBase = 1f; // XXX .7f
		droneTransitionSpeed = 2f;
	}
	
	public void setDroneTarget(float target){
		droneBase = target;
		droneTransitionSpeed = 2f;
	}

	public void menuToGame() {
		randomDrone = false;
		AudioEngine.i().setDroneTarget(0.4f);
	}

	public void gameToMenu() {
		AudioEngine.i().setDroneTarget(0f);
	}

	public void menuBegin() {
		AudioEngine.i().start();
	}
	
	private void sfxPlay(int index){
		sfx.get(index).play(sfxVolume());
	}

	public void sfxInc() {
		sfxPlay(9);
	}

	public void sfxShift() {
		sfxPlay(0);
	}

	public void sfxFreeze() {
		sfxPlay(4);
	}

	public void sfxSwapUp() {
		sfxPlay(11);
	}
	
	public void sfxSwapDown() {
		sfxPlay(10);
	}

	public void sfxCrush() {
		sfxPlay(7);
	}

	public void sfxMicroClock() {
		sfxPlay(3);
	}

	public void sfxClock() {
		sfxPlay(2);
	}

	public Action sfxSmallGlitchAsAction() {
		return Actions.run(new Runnable() {
			@Override
			public void run() {
				sfxSmallGlitch();
			}
		});
	}

	private void sfxSmallGlitch() {
		sfxPlay(6);
	}
	
	public Action sfxBigGlitchAsAction() {
		return Actions.run(new Runnable() {
			@Override
			public void run() {
				sfxBigGlitch();
			}
		});
	}

	private void sfxBigGlitch() {
		sfxPlay(5);
	}

}
