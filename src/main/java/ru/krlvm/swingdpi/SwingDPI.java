package ru.krlvm.swingdpi;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Collections;

/**
 * SwingDPI API
 *
 * API of SwingDPI
 *
 * SwingDPI allows you to scale your application for convenient using on HiDPI screens
 * Call SwingDPI.applyScalingAutomatically() on your application start for easy scaling
 * GitHub Page: https://github.com/krlvm/SwingDPI
 *
 * @version 1.1
 * @author krlvm
 */
public class SwingDPI {

    public static final String VERSION = "1.1.5";

    //is scale factor set
    private static boolean scaleFactorSet = false;
    //the applied scale factor, e.g. 1.25 when the system DPI scaling is 125%
    private static float scaleFactor = 1.0f;
    //default scale factor, 100% scale
    private static final float DEFAULT_SCALE_FACTOR = 1.0f;
    //is DPI scale applied
    private static boolean scaleApplied = false;
    //suffixes of values that should be scaled
    private static String[] SUFFIXES_FOR_SCALE = new String[] { "width", "height", "indent", "size", "gap" };

    /**
     * Automatically determines scale factor and applies scaling for all existing and new windows
     */
    public static void applyScalingAutomatically() {
        determineScaleFactor();
        if(scaleFactor != DEFAULT_SCALE_FACTOR) {
            setScaleApplied(true);
        }
    }

    /**
     * Determines and sets the system DPI scaling setting and retrieves scale factor
     *
     * @return DPI scale factor
     */
    public static float determineScaleFactor() {
        float resolution = Toolkit.getDefaultToolkit().getScreenResolution(); //gets the screen resolution in percent, i.e. system DPI scaling
        if(resolution != 100.0f) { //when the system DPI scaling is not 100%
            setScaleFactor(resolution / 96.0f); //divide the system DPI scaling by default (100%) DPI and get the scale factor
        }
        return scaleFactor;
    }

    /**
     * Applies/disables scale for new and existing frames
     *
     * @param apply - enable or disable scaling
     */
    public static void setScaleApplied(boolean apply) {
        setScaleApplied(apply, true);
    }

    /**
     * Applies/disables scale for new and existing frames (depending on param)
     *
     * @param apply - enable or disable scaling
     * @param scaleExistingFrames - enable or disable scaling for existing frames
     */
    public static void setScaleApplied(boolean apply, boolean scaleExistingFrames) {
        if(apply == scaleApplied) {
            return; //scale already applied/disabled
        }
        scaleApplied = apply;
        if(!apply) {
            setScaleFactor(1.0f); //after that, the scaling factor should be determined again
        }

        UIDefaults defaults = UIManager.getLookAndFeelDefaults(); //gets the Swing UI defaults - we will writing in them
        for(Object key : Collections.list(defaults.keys())) { //processing all default UI keys
            Object original = defaults.get(key);
            Object newValue = scale(key, original);
            if(newValue != null && newValue != original) {
                defaults.put(key, newValue); //updating defaults
            }
        }
        if(scaleExistingFrames) {
            for(Frame frame : Frame.getFrames()) { //gets all created frames
                if(!(frame instanceof JFrame)) {
                    return;
                }
                Dimension dimension = frame.getSize();
                frame.setSize(scale(dimension));
                for(Component component : ((JFrame) frame).getContentPane().getComponents()) {
                    dimension = component.getSize();
                    Dimension newDimension = scale(dimension);
                    if(component instanceof JTextField) {
                        component.setPreferredSize(newDimension);
                    } else {
                        component.setSize(newDimension);
                    }
                }
            }
        }
    }

    /**
     * Retrieves a boolean that determines whether scaling is applied or not.
     *
     * @return is scaling applied
     */
    public static boolean isScaleApplied() {
        return scaleApplied;
    }

    /**
     * Sets the scale factor
     *
     * @param scaleFactor - new scale factor
     */
    public static void setScaleFactor(float scaleFactor) {
        if(!scaleFactorSet) {
            scaleFactorSet = true;
        }
        SwingDPI.scaleFactor = scaleFactor;
    }

    /**
     * Retrieves the current scale factor
     *
     * @return scale factor
     */
    public static float getScaleFactor() {
        if(!scaleFactorSet) {
            determineScaleFactor();
        }
        return scaleFactor;
    }

    /**
     * Retrieve scaled version of a param from Swing UI defaults
     *
     * @param key - param key
     * @param original - original value
     * @return a scaled param version
     */
    private static Object scale(Object key, Object original) {
        if(original instanceof Font) {
            if(original instanceof FontUIResource && key.toString().endsWith(".font")) {
                int newSize = (int)(Math.round((float)((Font)original).getSize()) * scaleFactor);
                return new FontUIResource(((Font)original).getName(), ((Font)original).getStyle(), newSize);
            }
            return original;
        }
        if(original instanceof Integer) {
            if(!endsWithOneOf((key instanceof String) ? ((String)key).toLowerCase() : "")) {
                return original;
            }
            return (int)((Integer)original * scaleFactor);
        }
        return null;
    }

    /**
     * Scales dimension
     *
     * @param dimension - dimension to scale
     * @return a scaled version of the dimension
     */
    public static Dimension scale(Dimension dimension) {
        if(!scaleFactorSet) {
            return dimension;
        }
        dimension.setSize((int)(dimension.getWidth() * scaleFactor), (int)(dimension.getHeight() * scaleFactor));
        return dimension;
    }

    /**
     * Retrieve a scaled version of a dimension
     *
     * @param dimension - dimension to scale
     * @return a scaled version of the dimension
     */
    public static Dimension getScaledDimension(Dimension dimension) {
        if(!scaleFactorSet) {
            return dimension;
        }
        return new Dimension((int)(dimension.getWidth() * scaleFactor), (int)(dimension.getHeight() * scaleFactor));
    }

    public static Dimension scale(int width, int height) {
        return scale(new Dimension(width, height));
    }

    public static int scale(int i) {
        if(!scaleFactorSet) {
            return i;
        }
        return (int)(i*scaleFactor);
    }

    private static boolean endsWithOneOf(String text) {
        for(String suffix : SUFFIXES_FOR_SCALE) {
            if(suffix.endsWith(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve scaled version of dimension, scale algorithm of that is optimized for JFrame scaling
     *
     * @param dimension - dimension to scale
     * @return a scaled version of the dimension
     */
    public static Dimension scaleFrame(Dimension dimension) {
        if(!scaleFactorSet) {
            return dimension;
        }
        return scale((int)(dimension.width-(dimension.width * .2)), (int)(dimension.height-(dimension.height * .15)));
    }

    /**
     * If it's unable to scale font of specific component use this method
     *
     * @param component - component for scale
     */
    public static void scaleFont(Component component) {
        Font font = component.getFont();
        float size = font.getSize()*scaleFactor;
        component.setFont(font.deriveFont(size));
    }
}
