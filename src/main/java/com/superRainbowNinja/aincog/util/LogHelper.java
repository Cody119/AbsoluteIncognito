package com.superRainbowNinja.aincog.util;

import com.superRainbowNinja.aincog.refrence.Reference;

import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.*;


/**
 * Created by SuperRainbowNinja on 24/10/2016.
 *
 * TODO make this work properly
 */
public class LogHelper {
    public static final Marker MARKER = MarkerManager.getMarker(Reference.MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public static String getPosString(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
    }

    public static void log(Level level, Object msg) {
        LOGGER.log(level, MARKER, msg);
    }

    public static void errorLog(Object msg) {
        //log(Level.ERROR, msg);
        System.err.println(msg);
    }

    public static void debugLog(Object msg) {
        log(Level.DEBUG, msg);
    }

    public static void infoLog(Object msg) {
        log(Level.INFO, msg);
    }
}
