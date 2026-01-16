package org.team100.sim2026;

import java.util.function.IntSupplier;

public class Robot implements Actor, BallContainer {
    final Zone myZone;
    final Zone neutralZone;
    final Zone otherZone;
    final Hub myHub;
    final IntSupplier matchTimer;
    Zone location;
    int count;

    // this is null if we're not going anywhere
    // if it's not null, we complete the trip (to keep from changing our mind)
    Zone destination;
    int travelTimer;
    boolean active;
    // 4 chars
    String action = "";
    // 2 chars
    final String name;

    public Robot(
            String name,
            Zone myZone,
            Zone neutralZone,
            Zone otherZone,
            Hub myHub,
            int initialCount,
            IntSupplier matchTimer) {
        this.name = name;
        this.myZone = myZone;
        this.neutralZone = neutralZone;
        this.otherZone = otherZone;
        this.myHub = myHub;
        this.matchTimer = matchTimer;
        // initial location is my own zone
        this.location = myZone;
        count = initialCount;
    }

    public String header() {
        return String.format("%5.5s, %5.5s, %5.5s",
                name + "l", name + "ct", name + "act");
    }

    @Override
    public String toString() {
        return String.format("%5s, %5d, %5s",
                location.name, count(), action);
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public Runnable step() {
        return this::action;
    }

    void action() {
        if (matchTimer.getAsInt() < 20)
            // auton
            ferry();
        else
            teleop();

        // shoot();
        // lobToTheOtherZone();

    }

    void teleop() {
        if (active)
            ferry();
        else
            lob();

    }

    void lob() {
        if (destination != null) {
            // we're going somewhere, so keep going
            if (travelTimer > 0) {
                // still going
                --travelTimer;
                action = "move";
            } else {
                // we have arrived
                location = destination;
                destination = null;
                action = "arrv";
            }
        } else if (location == myZone) {
            // start driving to the neutral zone
            destination = neutralZone;
            // it takes 3 seconds to get there
            travelTimer = 3;
            action = "move";
        } else {
            if (count > 5) {
                // lob
                myZone.accept(count);
                count = 0;
                action = "lob";
            }
            // can intake at the same time.
            if (location.count() > 5) {
                int taken = location.take(10);
                count += taken;
            } else {
                // do nothing
            }

        }
    }

    void ferry() {
        if (destination != null) {
            // we're going somewhere, so keep going
            if (travelTimer > 0) {
                // still going
                --travelTimer;
                action = "move";
            } else {
                // we have arrived
                location = destination;
                destination = null;
                action = "arrv";
            }
        } else if (location == myZone) {
            // we're in our zone.
            if (count > 5) {
                // we have balls to shoot, so shoot them
                shoot();
                action = "shot";
            }
            // at the same time, we can pick up
            if (myZone.count() > 5) {
                // yes, intake them
                int taken = myZone.take(10);
                count += taken;
            } else {
                // no, start driving to the neutral zone
                destination = neutralZone;
                // it takes 3 seconds to get there
                travelTimer = 3;
            }
        } else {
            // we're in the neutral zone
            // for now let's ferry
            if (count > 50) {
                // time to go back
                destination = myZone;
                travelTimer = 3;
                action = "move";
            } else {
                if (location.count() > 5) {
                    int taken = location.take(10);
                    count += taken;
                    action = "intk";
                } else {
                    // nothing left here so go back
                    destination = myZone;
                    travelTimer = 3;
                    action = "move";
                }
            }

        }

    }

    /** Shoot everything in one second */
    void shoot() {
        myHub.accept(count);
        count = 0;
    }

    void lobToTheOtherZone() {
        // try to take some
        int taken = myZone.take(10);
        // taken balls are immediately available
        count += taken;
        // lob everything to the other side (this cannot be refused)
        otherZone.accept(count);
        count = 0;
    }
}
