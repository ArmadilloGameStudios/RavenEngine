package com.armadillogamestudios.storyteller.resource.location;

public class DateTime {
    private long minutes = 0L;

    public void passTime(int minutes) {
        this.minutes += minutes;
    }

    public void passTimeTo(long minute) {
        minutes = minute;
    }

    @Override
    public String toString() {
        // TODO make interesting
        return Long.toString(minutes);
    }

    public long getTime() {
        return minutes;
    }
}