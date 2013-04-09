package com.volumetricpixels.bans.util;

import java.util.Calendar;

import org.spout.api.scheduler.Task;

/**
 * A timer to delete any Deletable object
 */
public class DeletableTimer implements Runnable {
    private static Calendar c = Calendar.getInstance();

    private final long endMinutesSinceEpoch;
    private final Deletable timedDeletable;

    private Task spoutTaskObject;

    public DeletableTimer(final long end, final Deletable timed) {
        endMinutesSinceEpoch = end;
        timedDeletable = timed;
    }

    @Override
    public void run() {
        if (c.getTimeInMillis() / 1000 / 60 >= endMinutesSinceEpoch) {
            timedDeletable.delete();
            spoutTaskObject.cancel();
        }
    }

    public void setTask(final Task task) {
        spoutTaskObject = task;
    }
}
