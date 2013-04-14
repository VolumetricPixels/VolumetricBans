package com.volumetricpixels.bans.util;

import java.util.Calendar;

import org.spout.api.scheduler.Task;

/**
 * A timer to delete any Deletable object
 */
public class DeletableTimer implements Runnable {
    private static Calendar c = Calendar.getInstance();

    /** The time when the Deletable should be deleted */
    private final long endMinutesSinceEpoch;
    /** The Deletable being timed */
    private final Deletable timedDeletable;

    private Task spoutTaskObject;

    /**
     * Constructs a new DeletableTimer
     * 
     * @param end The time when the Deletable should be deleted
     * @param timed The Deletable to time the deletion of
     */
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
