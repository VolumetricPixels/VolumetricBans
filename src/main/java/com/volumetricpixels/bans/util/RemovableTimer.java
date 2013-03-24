package com.volumetricpixels.bans.util;

import java.util.Calendar;

import org.spout.api.scheduler.Task;

public class RemovableTimer implements Runnable {
	private static Calendar c = Calendar.getInstance();

	private long endMinutesSinceEpoch;
	private Removable timedRemovable;
	private Task spoutTaskObject;

	public RemovableTimer(long end, Removable timed) {
		endMinutesSinceEpoch = end;
		timedRemovable = timed;
	}

	@Override
	public void run() {
		if (((c.getTimeInMillis() / 1000) / 60) >= endMinutesSinceEpoch) {
			timedRemovable.remove();
			spoutTaskObject.cancel();
		}
	}

	public void setTask(Task task) {
		spoutTaskObject = task;
	}
}
