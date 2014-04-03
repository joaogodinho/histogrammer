package net.fernandezgodinho.histogrammer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageExample {

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("res/image.jpg"));
        Histogrammer histo = new Histogrammer(image);
        histo.calcHistogram();
        ImageIO.write(histo.getHistogramAsImage(2), "png", new File("res/histogram.png"));
    }
}
