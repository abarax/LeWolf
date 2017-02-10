package com.jaspervine.core;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class Time {

    public static final long SECOND = 1000000000L;
    private static double delta;

    public static long getTime(){
        return System.nanoTime();
    }

    public static double getDelta(){
        return delta;
    }

    public static void setDelta(double d) {
        delta = d;
    }
}
