package net.fernandezgodinho.histogrammer;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * The Class Histogrammer. Simple class to calculate an image histogram.
 * 
 * @author Joao Godinho
 */
public class Histogrammer {
    /** The Constant OPAQUE. ARGB*/
    private final static int OPAQUE = 0xFF000000;
    
    /** The Constant RED_COLOR. ARGB */
    private final static int RED_COLOR = 0x00FF0000;
    
    /** The Constant GREEN_COLOR. ARGB */
    private final static int GREEN_COLOR = 0x0000FF00;
    
    /** The Constant BLUE_COLOR. ARGB */
    private final static int BLUE_COLOR = 0x000000FF;
    
    /** The Constant COLOR_RANGE. */
    private final static int COLOR_RANGE = 256;
    
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
    
    /** The image. */
    private BufferedImage image;
    
    /** The img width. */
    private int imgWidth;
    
    /** The img height. */
    private int imgHeight;
    
    /**
     * The histogram. First dimension represents ARGB values, second dimension represents
     * range of colors.
     */
    private int[][] histogram = new int[4][COLOR_RANGE];
    
    /**
     * The percentage histogram. First dimension represents ARGB values, second dimension represents
     * range of colors.
     */
    private int[][] percentageHistogram;
    
    /**
     * Instantiates a new histogrammer. Receives a BufferedImage, sets image width and height
     * and initializes histogram array to 0.
     *
     * @param image the image
     */
    public Histogrammer(BufferedImage image) {
        this.image = image;
        this.imgWidth = this.image.getWidth();
        this.imgHeight = this.image.getHeight();
        for (int[] color : histogram) { Arrays.fill(color, 0); }
    }
    
    /**
     * Calculates the histogram of the image given in the constructor. For each pixel, gets its
     * ARGB value and increments the corresponding histogram array position.
     */
    public void calcHistogram() {
        for (int h = 0; h < imgHeight; h++) {
            for (int w = 0; w < imgWidth; w++) {
                int pixel = image.getRGB(w, h);
                histogram[0][BYTE_MASK & (pixel >> ALPHA_SHIFT)]++; // ALPHA
                histogram[1][BYTE_MASK & (pixel >> RED_SHIFT)]++;   // RED
                histogram[2][BYTE_MASK & (pixel >> GREEN_SHIFT)]++; // GREEN
                histogram[3][BYTE_MASK & (pixel >> BLUE_SHIFT)]++;  // BLUE
            }
        }
    }
    
    /**
     * Gets the histogram.
     *
     * @return the histogram
     */
    public int[][] getHistogram() {
        return histogram;
    }
    
    /**
     * Gets the percentage histogram. Creates a percentage histogram on the first call, returns the already
     * calculated percentage histogram on subsequent calls.
     *
     * @return the percentage histogram
     */
    public int[][] getPercentageHistogram() {
        if (percentageHistogram == null) {
            percentageHistogram = new int[4][COLOR_RANGE];
            for (int i = 0; i < COLOR_RANGE; i++) {
                percentageHistogram[0][i] = (int) Math.round(histogram[0][i] * 100.0 / (imgHeight * imgWidth));
                percentageHistogram[1][i] = (int) Math.round(histogram[1][i] * 100.0 / (imgHeight * imgWidth));
                percentageHistogram[2][i] = (int) Math.round(histogram[2][i] * 100.0 / (imgHeight * imgWidth));
                percentageHistogram[3][i] = (int) Math.round(histogram[3][i] * 100.0 / (imgHeight * imgWidth));
            }    
        }
        return percentageHistogram;
    }
    
    /**
     * Gets the histogram as image.
     *
     * @return the histogram as image
     */
    public BufferedImage getHistogramAsImage(int scale) {
        int width = COLOR_RANGE * scale;
        int height = 100/*%*/ * scale;
        BufferedImage image = new BufferedImage(COLOR_RANGE * scale, 100/*%*/ * scale, BufferedImage.TYPE_INT_ARGB);
        int[][] histogram = getPercentageHistogram();
        int[] colorArr = new int[scale];
        
        for (int i = 0, correctY, currPixel; i < COLOR_RANGE; i++) {
            // OPAQUE
            correctY = (100 - histogram[0][i]) * scale;
            if (correctY == 100 * scale) { correctY--; }
            currPixel = image.getRGB(i * scale, correctY);
            Arrays.fill(colorArr, OPAQUE | currPixel);
            image.setRGB(i * scale, correctY, scale, 1, colorArr, 0, scale);
            // RED
            correctY = (100 - histogram[1][i]) * scale;
            if (correctY == 100 * scale) { correctY--; }
            currPixel = image.getRGB(i * scale, correctY);
            Arrays.fill(colorArr, OPAQUE | RED_COLOR | currPixel);
            image.setRGB(i * scale, correctY, scale, 1, colorArr, 0, scale);
            // GREEN
            correctY = (100 - histogram[2][i]) * scale;
            if (correctY == 100 * scale) { correctY--; }
            currPixel = image.getRGB(i * scale, correctY);
            Arrays.fill(colorArr, OPAQUE | GREEN_COLOR | currPixel);
            image.setRGB(i * scale, correctY, scale, 1, colorArr, 0, scale);
            // BLUE
            correctY = (100 - histogram[3][i]) * scale;
            if (correctY == 100 * scale) { correctY--; }
            currPixel = image.getRGB(i * scale, correctY);
            Arrays.fill(colorArr, OPAQUE | BLUE_COLOR | currPixel);
            image.setRGB(i * scale, correctY, scale, 1, colorArr, 0, scale);
        }
        
        // Fill the rest with grey
        for (int h = 0; h < height; h++) {
            for (int w = 0, currPixel; w < width; w++) {
                currPixel = image.getRGB(w, h);
                if (currPixel == 0) {
                    image.setRGB(w, h, OPAQUE | 
                            (RED_COLOR - (100 << RED_SHIFT)) |
                            (GREEN_COLOR - (100 << GREEN_SHIFT)) | 
                            (BLUE_COLOR - (100 << BLUE_SHIFT)));
                }
            }
        }
        return image;
    }
    
    public BufferedImage getHistogramAsImage() {
        return this.getHistogramAsImage(1);
    }
}
