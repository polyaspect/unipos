package unipos.printer.components.shared.uniposInterpreter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class ImageScaler {

    private ImageIcon originalImage;
    private ImageIcon scaledImage;

    public ImageScaler(Image image) {
        this.originalImage = new ImageIcon(image);
    }

    public ImageScaler(String fileName) {
        originalImage = new ImageIcon(fileName);
    }

    public void createScaledImage(int size, ScalingDirection scalingDirection) {
        if (scalingDirection == ScalingDirection.HORIZONTAL) {
            scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(size, -1, Image.SCALE_SMOOTH));
        } else {
            scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(-1, size, Image.SCALE_SMOOTH));
        }
    }

    public void createScaledImage(int size, ScalingDirection scalingDirection, int scale) {
        if (scalingDirection == ScalingDirection.HORIZONTAL) {
            scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(size, -1, scale));
        } else {
            scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(-1, size, scale));
        }
    }

    public void createScaledImage(int width, int height, ScaleType scaleType) {
        int imageWidth = originalImage.getImage().getWidth(null);
        int imageHeight = originalImage.getImage().getHeight(null);
        double originalImageRatio = imageWidth / (double) imageHeight;
        double scaledImageRatio = width / (double) height;

        if(scaleType == ScaleType.FIT) {
            if(imageHeight - (Math.abs(imageWidth - width) / originalImageRatio) <= height) {
                scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH));
            } else if(imageWidth - (Math.abs(imageHeight - height) * originalImageRatio) <= width) {
                scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(-1, height, Image.SCALE_SMOOTH));
            }
        } else if(scaleType == ScaleType.FILL) {
            if(imageHeight - (Math.abs(imageWidth - width) / originalImageRatio) >= height) {
                scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH));
                int thumbHeight = scaledImage.getImage().getHeight(null);

                // Crop the image
                scaledImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(scaledImage.getImage().getSource(), new CropImageFilter(0, (thumbHeight-height)/2, width, height))));
            } else if(imageWidth - (Math.abs(imageHeight - height) * originalImageRatio) >= width) {
                scaledImage = new ImageIcon(originalImage.getImage().getScaledInstance(-1, height, Image.SCALE_SMOOTH));
                int thumbWidth = scaledImage.getImage().getWidth(null);

                // Crop the image
                scaledImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(scaledImage.getImage().getSource(), new CropImageFilter((thumbWidth-width)/2, 0, width, height))));
            }
        }
    }

    public void saveScaledImage(File file, ImageType imageType) {
        if (scaledImage != null) {
            BufferedImage bi = new BufferedImage(scaledImage.getIconWidth(), scaledImage.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(scaledImage.getImage(), 0, 0, null);
            try {
                ImageIO.write(bi, "bmp", file);
            } catch (IOException ioe) {
                System.out.println("Error occured saving scaled image");
            }
        } else {
            System.out.println("Scaled image has not yet been created");
        }
    }

    public void saveOriginalImage(File file, ImageType imageType) {
        if (originalImage != null) {
            BufferedImage bi = new BufferedImage(originalImage.getIconWidth(), originalImage.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(originalImage.getImage(), 0, 0, null);
            try {
                ImageIO.write(bi, imageType.value(), file);
            } catch (IOException ioe) {
                System.out.println("Error occured saving original image");
            }
        } else {
            System.out.println("Original image has not yet been created");
        }
    }

    // ENUMS
    public enum ScalingDirection {VERTICAL, HORIZONTAL};
    public enum ScaleType {FIT, FILL};
    public enum ImageType {
        IMAGE_JPEG ("jpeg"),
        IMAGE_JPG ("jpg"),
        IMAGE_PNG ("png");

        private String value = null;

        ImageType(String value) {
            this.value = value;
        }

        String value() {
            return value;
        }
    };
}   