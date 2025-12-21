import java.util.Scanner;
import java.io.File;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class img_asci {
    //Researched and coded by Paritosh Tripathi

    // all functions in here
    // /there is defined utility class at line 44 that gives all the functions for usage:
        // utility.read(img) this function helps to read the image and return buffered image object
        // utility.scaleimage(img,width,height) this uses awt.graphics2d to scale down the image for better representation 
        // utility.img_ascii(img,args) this is the complete functioning of image to ascii , it turns image to gray scale and call gray_ascii()
        // utility.gray_ascii(img,r,g,b) this maps pixels to ascii characters
    public static void main(String args[]) {
        // main code for my img to ascii
        System.out.println(args[0]);
        if(args[0].equals("-h")){
            System.out.println("for help");
            System.out.println("java .class_file -h");
            System.out.println("arguments seperated by space");
            System.out.println("java .class_file image-path brightness_of_output(integer<255)  console-width(integer for width of the output usually 90)");
        }
        
        utility utils = new utility();
        if(!args[0].equals("-h")){
        BufferedImage img = utils.read(args[0]);


        if (img != null) {
            // scale image to fit console
            int consoleWidth = (args.length==3)? Integer.parseInt(args[2]):90; // width in characters
           
            int newHeight = img.getHeight() * consoleWidth / img.getWidth() /2; // adjust for character height
            System.out.println(newHeight);
            BufferedImage scaledImg = utils.scaleImage(img, consoleWidth, newHeight);

            // print ASCII art
            utils.img_ascii(scaledImg,args);
        } else {
            System.out.println("Failed to read image.");
        }
    }
}
}

class utility {

    public BufferedImage read(String path) {
        try {
            File f = new File(path);
            BufferedImage img = ImageIO.read(f);
            if (img == null) {
                System.out.println("invalid path");
            }
            return img;
        } catch (Exception e) {
            System.out.println("error occurred: " + e.getMessage());
            return null; // must return something
        }
    }

    // scale the image
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    // convert image to ASCII
    public void img_ascii(BufferedImage img, String args[]) {
        int h = img.getHeight();
        int w = img.getWidth();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pix = img.getRGB(x, y);// here pix is a 32 bit integer representing alpha r g b values
                int r = (pix >> 16) & 0xff;//r is the bit from 16-23 henece >> is done for shifting bits to r 
                int g = (pix >> 8) & 0xff;
                int b = pix & 0xff;
                int brightness=(args.length == 3) ? Integer.parseInt(args[1]) : 20;
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b); // convert to grayscale , hee gray is the number which is sum of rgb values with weights by NSTC
                gray = Math.min(255, gray +brightness);//increases the brightness
                gray_ascii(gray,r,g,b);
            }
            System.out.println(); // new line after each row
        }
    }

    // map grayscale to ASCII
    public void gray_ascii(int gray, int r, int g, int b) {
        char[] asc = {
                ' ', '`', '.', '^', ':', '-', '~', '"', '\'', '!', '/', '\\', '|',
                '(', ')', '?', '!', '=', '+', '*', '#', '%', '&', '$', '@'
        };

        int index = (int) ((gray / 255.0) * (asc.length - 1));
        // index = Math.max(0, Math.min(index, asc.length - 1)); // clamp index
       System.out.print(String.format("\u001B[38;2;%d;%d;%dm%c\u001B[0m", r, g, b, asc[index])) ;// use print, not println
    }
}
