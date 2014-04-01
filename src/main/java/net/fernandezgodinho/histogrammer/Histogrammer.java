package net.fernandezgodinho.histogrammer;

import java.awt.image.BufferedImage;
import java.util.Arrays;


// TODO change to percentage, finish javadoc
/**
 * The Class Histogrammer.
 * 
 * @author Joao Godinho
 */
public class Histogrammer {
    /** The Constant BYTE_MASK. */
    private final static int BYTE_MASK = 0xFF;
    
    /** The Constant ALPHA_SHIFT. */
    private final static int ALPHA_SHIFT = 24;
    
    /** The Constant RED_SHIFT. */
    private final static int RED_SHIFT = 16;
    
    /** The Constant GREEN_SHIFT. */
    private final static int GREEN_SHIFT = 8;
    
    /** The Constant BLUE_SHIFT. */
    private final static int BLUE_SHIFT = 0;
    
    private BufferedImage image;
    private int[][] histogram = new int[4][256];
    
    public Histogrammer(BufferedImage image) {
        this.image = image;
        for (int[] color : histogram) { Arrays.fill(color, 0); }
    }
    
    public void calcHistogram() {
        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                int pixel = image.getRGB(w, h);
                histogram[0][BYTE_MASK & (pixel >> ALPHA_SHIFT)]++; // ALPHA
                histogram[1][BYTE_MASK & (pixel >> RED_SHIFT)]++;   // RED
                histogram[2][BYTE_MASK & (pixel >> GREEN_SHIFT)]++; // GREEN
                histogram[3][BYTE_MASK & (pixel >> BLUE_SHIFT)]++;  // BLUE
            }
        }
    }
    
    public int[][] getHistogram() {
        return histogram;
    }
}
