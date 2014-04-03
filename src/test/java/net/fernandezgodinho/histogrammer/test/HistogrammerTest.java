package net.fernandezgodinho.histogrammer.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.fernandezgodinho.histogrammer.Histogrammer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class HistogrammerTest.
 * 
 * @author Joao Godinho
 */
public class HistogrammerTest {
    /** The Constant COLOR_RANGE. */
    private final static int COLOR_RANGE = 256;
    
    /** The Constant IMG_WIDTH. */
    private final static int IMG_WIDTH = 500;
    
    /** The Constant IMG_HEIGHT. */
    private final static int IMG_HEIGHT = 500;
    
    /** The Constant RED_SHIFT. */
    private final static int RED_SHIFT = 16;
    
    /** The Constant GREEN_SHIFT. */
    private final static int GREEN_SHIFT = 8;
    
    /** The Constant BLUE_SHIFT. */
    private final static int BLUE_SHIFT = 0;
    
    /** The Constant OPAQUE. ARGB*/
    private final static int OPAQUE = 0xFF000000;
    
    /** The Constant RED_COLOR. ARGB */
    private final static int RED_COLOR = 0x00FF0000;
    
    /** The Constant GREEN_COLOR. ARGB */
    private final static int GREEN_COLOR = 0x0000FF00;
    
    /** The Constant BLUE_COLOR. ARGB */
    private final static int BLUE_COLOR = 0x000000FF;
    
    /** The full black img. */
    private BufferedImage blackImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The full white img. */
    private BufferedImage whiteImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The full red img. */
    private BufferedImage redImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The full green img. */
    private BufferedImage greenImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The full blue img. */
    private BufferedImage blueImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

    /** The red gradient img. */
    private BufferedImage redGradientImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The green gradient img. */
    private BufferedImage greenGradientImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** The blue gradient img. */
    private BufferedImage blueGradientImg = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    
    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() {
        int[] imgArr = new int[IMG_WIDTH * IMG_HEIGHT];
        
        // BLACK
        Arrays.fill(imgArr, OPAQUE);
        blackImg.setRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, imgArr, 0, IMG_WIDTH);
        // WHITE
        Arrays.fill(imgArr, OPAQUE | RED_COLOR | GREEN_COLOR | BLUE_COLOR);
        whiteImg.setRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, imgArr, 0, IMG_WIDTH);
        // RED
        Arrays.fill(imgArr, OPAQUE | RED_COLOR);
        redImg.setRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, imgArr, 0, IMG_WIDTH);
        // GREEN
        Arrays.fill(imgArr, OPAQUE | GREEN_COLOR);
        greenImg.setRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, imgArr, 0, IMG_WIDTH);
        // BLUE
        Arrays.fill(imgArr, OPAQUE | BLUE_COLOR);
        blueImg.setRGB(0, 0, IMG_WIDTH, IMG_HEIGHT, imgArr, 0, IMG_WIDTH);
        
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            int[] line = new int[IMG_WIDTH];
            
            Arrays.fill(line, OPAQUE | normalized << RED_SHIFT);
            redGradientImg.setRGB(0, h, IMG_WIDTH, 1, line, 0, IMG_WIDTH);
            
            Arrays.fill(line, OPAQUE | normalized << GREEN_SHIFT);
            greenGradientImg.setRGB(0, h, IMG_WIDTH, 1, line, 0, 0);
            
            Arrays.fill(line, OPAQUE | normalized << BLUE_SHIFT);
            blueGradientImg.setRGB(0, h, IMG_WIDTH, 1, line, 0, 0);
        }
    }
    
    /**
     * Test empty histogram on image.
     */
    @Test
    public void testEmpty() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(redImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        actualHistogram = histogram.getHistogram();
        
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
    
    /**
     * Test black filled image.
     */
    @Test
    public void testBlackFill() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(blackImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[1][0] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[2][0] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[3][0] = IMG_WIDTH * IMG_HEIGHT;
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][0] = 100;
        expectedHistogram[2][0] = 100;
        expectedHistogram[3][0] = 100;
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
    
    /**
     * Test white filled image.
     */
    @Test
    public void testWhiteFill() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(whiteImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[1][255] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[2][255] = IMG_WIDTH * IMG_HEIGHT;
        expectedHistogram[3][255] = IMG_WIDTH * IMG_HEIGHT;
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][255] = 100;
        expectedHistogram[2][255] = 100;
        expectedHistogram[3][255] = 100;
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }

    /**
     * Test red filled image.
     */
    @Test
    public void testRedFill() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(redImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[1][255] = IMG_WIDTH * IMG_HEIGHT; // RED
        expectedHistogram[2][0] = IMG_WIDTH * IMG_HEIGHT;   // GREEN
        expectedHistogram[3][0] = IMG_WIDTH * IMG_HEIGHT;   // BLUE
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][255] = 100;
        expectedHistogram[2][0] = 100;
        expectedHistogram[3][0] = 100;
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }

    /**
     * Test green filled image.
     */
    @Test
    public void testGreenFill() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(greenImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[1][0] = IMG_WIDTH * IMG_HEIGHT;   // RED
        expectedHistogram[2][255] = IMG_WIDTH * IMG_HEIGHT; // GREEN
        expectedHistogram[3][0] = IMG_WIDTH * IMG_HEIGHT;   // BLUE
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][0] = 100;
        expectedHistogram[2][255] = 100;
        expectedHistogram[3][0] = 100;
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
    
    /**
     * Test blue filled image.
     */
    @Test
    public void testBlueFill() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(blueImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[1][0] = IMG_WIDTH * IMG_HEIGHT;   // RED
        expectedHistogram[2][0] = IMG_WIDTH * IMG_HEIGHT;   // GREEN
        expectedHistogram[3][255] = IMG_WIDTH * IMG_HEIGHT; // BLUE
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][0] = 100;
        expectedHistogram[2][0] = 100;
        expectedHistogram[3][255] = 100;
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
    
    /**
     * Test red gradient image.
     */
    @Test
    public void testRedGradient() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(redGradientImg);
        BufferedImage histogramImg;
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[2][0] = IMG_WIDTH * IMG_HEIGHT;   // GREEN
        expectedHistogram[3][0] = IMG_WIDTH * IMG_HEIGHT;   // BLUE
        // fills with the expected the gradient
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[1][normalized] += IMG_WIDTH;
        }
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        Arrays.fill(expectedHistogram[1], 0);
        expectedHistogram[2][0] = 100;
        expectedHistogram[3][0] = 100;
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[1][normalized] += (int) Math.round(IMG_WIDTH * 100.0 / (IMG_WIDTH * IMG_HEIGHT));
        }
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        histogramImg = histogram.getHistogramAsImage(2);
        File output = new File("redGradientHisto.png");
        try {
            ImageIO.write(histogramImg, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test green gradient image.
     */
    @Test
    public void testGreenGradient() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(greenGradientImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[1][0] = IMG_WIDTH * IMG_HEIGHT;   // RED
        expectedHistogram[3][0] = IMG_WIDTH * IMG_HEIGHT;   // BLUE
        // fills with the expected the gradient
        for (int h = 0; h < IMG_HEIGHT; h++) {
            
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[2][normalized] += IMG_WIDTH;
        }
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][0] = 100;
        Arrays.fill(expectedHistogram[2], 0);
        expectedHistogram[3][0] = 100;
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[2][normalized] += (int) Math.round(IMG_WIDTH * 100 / (IMG_WIDTH * IMG_HEIGHT));
        }
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
    
    /**
     * Test green gradient image.
     */
    @Test
    public void testBlueGradient() {
        int[][] expectedHistogram = new int[4][COLOR_RANGE];
        int[][] actualHistogram;
        Histogrammer histogram = new Histogrammer(blueGradientImg);
        
        for (int[] color : expectedHistogram) { Arrays.fill(color, 0); }
        expectedHistogram[0][255] = IMG_WIDTH * IMG_HEIGHT; // ALPHA
        expectedHistogram[1][0] = IMG_WIDTH * IMG_HEIGHT;   // RED
        expectedHistogram[2][0] = IMG_WIDTH * IMG_HEIGHT;   // GREEN
        // fills with the expected the gradient
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[3][normalized] += IMG_WIDTH;
        }
        histogram.calcHistogram();
        actualHistogram = histogram.getHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
        
        expectedHistogram[0][255] = 100;
        expectedHistogram[1][0] = 100;
        expectedHistogram[2][0] = 100;
        Arrays.fill(expectedHistogram[3], 0);
        for (int h = 0; h < IMG_HEIGHT; h++) {
            int normalized = (int) (h * COLOR_RANGE / IMG_HEIGHT);
            expectedHistogram[3][normalized] += (int) Math.round(IMG_WIDTH * 100 / (IMG_WIDTH * IMG_HEIGHT));
        }
        actualHistogram = histogram.getPercentageHistogram();
        Assert.assertArrayEquals(expectedHistogram, actualHistogram);
    }
}
