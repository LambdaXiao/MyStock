
package com.mystockchart_lib.charting.utils;

/**
 * Point encapsulating two double values.
 *
 */
public class PointD {

    public double x;
    public double y;

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * returns a string representation of the object
     */
    @Override
    public String toString() {
        return "PointD, x: " + x + ", y: " + y;
    }
}
