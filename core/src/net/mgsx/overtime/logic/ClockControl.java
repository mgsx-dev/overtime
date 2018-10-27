package net.mgsx.overtime.logic;

import java.util.Calendar;

import net.mgsx.overtime.ui.ClockGroup;

public class ClockControl {

	static final int MAX_TIME = 24 * 60;

	private int itime;
	private float time;
	private ClockGroup ui;
	private GameState state;
	
	public ClockControl(ClockGroup ui) {
		this.ui = ui;
		state = GameState.TIME_RUN;
	}
	
	public void setCurrentTime()
	{
		Calendar c = Calendar.getInstance();
		int h = c.get(Calendar.HOUR_OF_DAY);
		int m = c.get(Calendar.MINUTE);
		setTime(h, m);
	}

	public void setTime(int h, int m) {
		setTime(h * 60 + m);
	}
	
	public void setTime(int value){
		this.itime = value;
		apply();
	}
	
	public void increment(int inc, int rank)
	{
		int mul = new int[]{1, 10, 60, 600}[rank];
		int toAdd = mul * inc;
		if(rank == 3){
			int h1 = itime / 600;
			if(inc > 0 && h1 == 2){
				toAdd = 240;
			}else if(inc < 0 && h1 == 0){
				toAdd = -240;
			}
		}
		increment(toAdd);
	}
	public void increment(int inc)
	{
		if(state == GameState.TIME_RUN){
			itime += inc;
			if(inc < 0 && itime == 0){
				state = GameState.TIME_ZERO;
			}
			else if(itime == MAX_TIME){
				state = GameState.TIME_TWENTY_FOUR;
			}else{
				itime = ((itime % MAX_TIME) + MAX_TIME) % MAX_TIME;
			}
			apply();
		}
	}
	
	private void apply()
	{
		int h = itime / 60;
		int m = itime % 60;
		ui.setTime(h/10, h%10, m/10, m%10);
	}
	
	public GameState update(float delta)
	{
		if(state == GameState.TIME_RUN){
			time += delta; // TODO speed ?
			if(time >= 1){
				time = 0;
			}
			ui.setDotsState(time < .5f);
		}
		// XXX ui.apply();
		return state;
	}
}
