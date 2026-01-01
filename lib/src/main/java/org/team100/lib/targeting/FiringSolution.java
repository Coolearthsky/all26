package org.team100.lib.targeting;

/**
 * Solution to a targeting problem.
 * 
 * @param range in meters
 * @param tof   time of flight in seconds
 */
public record FiringSolution(double range, double tof) {
}