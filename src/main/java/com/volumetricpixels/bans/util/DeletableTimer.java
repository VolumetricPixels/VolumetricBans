package com.volumetricpixels.bans.util;

import java.util.Calendar;

import org.spout.api.scheduler.Task;

public class DeletableTimer implements Runnable {
    private static Calendar c = Calendar.getInstance();

    private long endMinutesSinceEpoch;
    private Deletable timedDeletable;
    private Task spoutTaskObject;

    public DeletableTimer(long end, Deletable timed) {
        endMinutesSinceEpoch = end;
        timedDeletable = timed;
    }

    @Override
    public void run() {
        if (((c.getTimeInMillis() / 1000) / 60) >= endMinutesSinceEpoch) {
            timedDeletable.delete();
            spoutTaskObject.cancel();
        }
    }

    public void setTask(Task task) {
        spoutTaskObject = task;
    }
}
