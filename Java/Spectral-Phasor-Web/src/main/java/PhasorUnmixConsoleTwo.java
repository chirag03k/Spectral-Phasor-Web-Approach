package main.java;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.gui.NewImage;
import ij.io.TiffEncoder;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PhasorUnmixConsoleTwo {

    // the plot is imp and ip
    ImagePlus imp;
    ImageCanvas canvas;
    ImageProcessor ip;

    //imp2 is the original image
    ImagePlus imp2;

    // counter for selecting the reference points manually
    int clik = 0;
    //Array for recording the phasor cloud
    int[][] pixels ;
    //reference points array
    int rx[] = new int[4];
    int ry[] = new int[4];
    //Original image dimensionfs without ROI
    int Dim_x, Dim_y;
    // imp for spectrum plot
    ImagePlus imp_plot = null;
    //    ImageProcessor ip3 = null;
    private double minPxlPctl = 0.0001;    // signal (percentile); intensity image is
    private double maxPxlPctl = 0.9990;    // scaled to display minPxlPctl -> maxPxlPctl
    private double X1_max, X2_max, X3_max;
    boolean show_fraction = false;
    private int K;
    int phasor_dimX, phasor_dimY, phasor_dim=400;
    int components = 3;
    int config;

    int shiftx = 28;
    int shifty = 10;

    public void run(String path, String outfolder, String specifics, String coords){
        new File(outfolder).mkdir();
        Spectral_Phasor_Console toBeUnmixed = new Spectral_Phasor_Console();
        toBeUnmixed.run(toBeUnmixed.consoleSetup(path, null, specifics, false));
        this.imp = toBeUnmixed.finishedPhasor;
        this.ip = this.imp.getProcessor();

        Rectangle r = ip.getRoi();
        MetaData meta = new MetaData(imp);

        //original image
        this.imp2 = toBeUnmixed.imp;
        ImageStack ip2 = imp2.getStack();
        K = ip2.getSize();

        phasor_dimY = ip.getHeight();
        phasor_dimX = ip.getWidth();
        pixels= new int[ phasor_dimX][ phasor_dimY];

        //recording array for the phasor cloud
        for (int xx = 0; xx < phasor_dimX; xx++) {
            for (int yy = 0; yy < phasor_dimY; yy++) {
                pixels[xx][yy] = ip.getPixel(xx, yy);

            }
        }

        // No phasor reference because this is done online with a bunch of different people
        // clicking is handled by the JS on the website. The coordinates are given straight to the java application
        // look at Handler.java for the coordinate format
        String[] splitted = coords.split(",");
        rx[1] = Integer.parseInt(splitted[0]);
        ry[1] = Integer.parseInt(splitted[1]);
        rx[2] = Integer.parseInt(splitted[2]);
        ry[2] = Integer.parseInt(splitted[3]);
        rx[3] = Integer.parseInt(splitted[4]);
        ry[3]= Integer.parseInt(splitted[5]);
        if(splitted[6].equals("t")) {
            show_fraction = true;
        }
        // The unmixing is done below (not in its own method as originally programmed)
        // Dimensions of original image
        Dim_x = ip2.getWidth();
        Dim_y = ip2.getHeight();

        ImagePlus X1 = NewImage.createRGBImage("X1", Dim_x, Dim_y, 1, NewImage.FILL_BLACK);
        ImageProcessor X1_ip = X1.getProcessor();
        double X1_I = 0;

        ImagePlus X2 = NewImage.createRGBImage("X2", Dim_x, Dim_y, 1, NewImage.FILL_BLACK);
        ImageProcessor X2_ip = X2.getProcessor();
        double X2_I = 0;

        ImagePlus X3 = NewImage.createRGBImage("X3", Dim_x, Dim_y,
                1, NewImage.FILL_BLACK);
        ImageProcessor X3_ip = X3.getProcessor();
        double X3_I = 0;

        ImagePlus Overlay = NewImage.createRGBImage("Overlay", Dim_x, Dim_y,
                1, NewImage.FILL_BLACK);
        ImageProcessor Overlay_ip = Overlay.getProcessor();


        ImagePlus A1 = NewImage.createFloatImage("A1", Dim_x, Dim_y,
                1, NewImage.FILL_BLACK);
        ImageProcessor A1_ip = A1.getProcessor();
        double A1_I = 0;

        ImagePlus A2 = NewImage.createFloatImage("A2", Dim_x, Dim_y,
                1, NewImage.FILL_BLACK);
        ImageProcessor A2_ip = A2.getProcessor();
        double A2_I = 0;

        ImagePlus A3 = NewImage.createFloatImage("A3", Dim_x, Dim_y,
                1, NewImage.FILL_BLACK);
        ImageProcessor A3_ip = A3.getProcessor();
        double A3_I = 0;

        double XO_I = 0;
        double phasor_r[] = meta.getPhasorMAP_r();
        double phasor_i[] = meta.getPhasorMAP_i();

        double a, c;
        double alpha[][] = new double[4][Dim_x * Dim_y];
        double alpha1[] = new double[Dim_x * Dim_y];
        double alpha2[] = new double[Dim_x * Dim_y];
        double alpha3[] = new double[Dim_x * Dim_y];
        double alpha0[] = new double[Dim_x * Dim_y];
        double count[] = new double[Dim_x * Dim_y];
        double alpha_max[] = new double[4];
        double alpha_min[] = new double[4];

        for (int i = 1; i < Dim_x * Dim_y; i++) {
            if (phasor_r[i] != -2 && phasor_i[i] != -2) {
                if (r.contains(phasor_r[i], phasor_i[i])) {

                    count[i] = 0;
                    for (int z = 1; z < K; z++) {
                        count[i] += ip2.getVoxel((int) (i - Math.floor(i / Dim_x) * Dim_x), (int) Math.floor(i / Dim_x), z);
                    }

                    a = Math.abs(rx[2] * ry[1] - rx[1] * ry[2] + rx[3] * ry[2] - rx[2] * ry[3] + rx[1] * ry[3] - rx[3] * ry[1]);
                    alpha[1][i] = Math.abs(rx[2] * phasor_i[i] - phasor_r[i] * ry[2] + rx[3] * ry[2] - rx[2] * ry[3] + phasor_r[i] * ry[3] - rx[3] * phasor_i[i]) / a;
                    alpha[2][i] = Math.abs(rx[3] * phasor_i[i] - phasor_r[i] * ry[3] + rx[1] * ry[3] - rx[3] * ry[1] + phasor_r[i] * ry[1] - rx[1] * phasor_i[i]) / a;
                    alpha[3][i] = Math.abs(rx[1] * phasor_i[i] - phasor_r[i] * ry[1] + rx[2] * ry[1] - rx[1] * ry[2] + phasor_r[i] * ry[2] - rx[2] * phasor_i[i]) / a;

                    for (int j = 1; j <= 3; j++) {
                        if (alpha[j][i] > 1) {
                            alpha[j][i] = 1;
                        }
                        if (alpha[j][i] < 0) {
                            alpha[j][i] = 0;

                        }
                    }
                    c = alpha[1][i] + alpha[2][i] + alpha[3][i];
                    if (c > 1) {
                        alpha[1][i] = alpha[1][i] / c;
                        alpha[2][i] = alpha[2][i] / c;
                        alpha[3][i] = alpha[3][i] / c;
                    }

                    alpha1[i] = alpha[1][i] * count[i];
                    alpha2[i] = alpha[2][i] * count[i];
                    alpha3[i] = alpha[3][i] * count[i];
                    alpha0[i] = count[i];
                }
            }


        }

        Arrays.sort(alpha0);
        Arrays.sort(alpha1);
        Arrays.sort(alpha2);
        Arrays.sort(alpha3);


        alpha_min[0] = alpha0[(int) (alpha0.length * minPxlPctl)];
        alpha_max[0] = alpha0[(int) (alpha0.length * maxPxlPctl)];


        alpha_min[1] = alpha1[(int) (alpha1.length * minPxlPctl)];
        alpha_max[1] = alpha1[(int) (alpha1.length * maxPxlPctl)];

        alpha_min[2] = alpha2[(int) (alpha2.length * minPxlPctl)];
        alpha_max[2] = alpha2[(int) (alpha2.length * maxPxlPctl)];

        alpha_min[3] = alpha3[(int) (alpha3.length * minPxlPctl)];
        alpha_max[3] = alpha3[(int) (alpha3.length * maxPxlPctl)];


        int xx, yy;
        double alpha_t[] = new double[4];
        int RGBpxl[] = new int[3];

        for (int i = 1; i < Dim_x * Dim_y; i++) {

            if (phasor_r[i] != -2 && phasor_i[i] != -2) {

                if (r.contains(phasor_r[i], phasor_i[i])) {

                    X1_I = (count[i] * alpha[1][i]);

                    X2_I = (count[i] * alpha[2][i]);

                    X3_I = (int) (count[i] * alpha[3][i]);

                    alpha_t[1] += alpha[1][i];
                    alpha_t[2] += alpha[2][i];
                    alpha_t[3] += alpha[3][i];
                    xx = (int) (i - Math.floor(i / Dim_x) * Dim_x);
                    yy = (int) Math.floor(i / Dim_x);

                    RGBpxl[0] = (int) ((X1_I - alpha_min[1]) * 255 / (alpha_max[1] - alpha_min[1]));
                    if (RGBpxl[0] > 255) {
                        RGBpxl[0] = 255;
                    }
                    if (RGBpxl[0] < 0) {
                        RGBpxl[0] = 0;
                    }
                    RGBpxl[1] = 0;
                    RGBpxl[2] = 0;

                    X1_ip.putPixel(xx, yy, RGBpxl);

                    RGBpxl[0] = 0;
                    RGBpxl[1] = (int) ((X2_I - alpha_min[2]) * 255 / (alpha_max[2] - alpha_min[2]));
                    RGBpxl[2] = 0;

                    if (RGBpxl[1] > 255) {
                        RGBpxl[1] = 255;
                    }
                    if (RGBpxl[1] < 0) {
                        RGBpxl[1] = 0;
                    }

                    X2_ip.putPixel(xx, yy, RGBpxl);
                    RGBpxl[0] = 0;
                    RGBpxl[1] = 0;
                    RGBpxl[2] = (int) ((X3_I - alpha_min[3]) * 255 / (alpha_max[3] - alpha_min[3]));

                    if (RGBpxl[2] > 255) {
                         RGBpxl[2] = 255;
                    }
                    if (RGBpxl[2] < 0) {
                         RGBpxl[2] = 0;
                    }

                    X3_ip.putPixel(xx, yy, RGBpxl);
                    }

                    RGBpxl[0] = (int) ((X1_I - alpha_min[0]) * 255 / (alpha_max[0] - alpha_min[0]));
                    RGBpxl[1] = (int) ((X2_I - alpha_min[0]) * 255 / (alpha_max[0] - alpha_min[0]));
                    RGBpxl[2] = (int) ((X3_I - alpha_min[0]) * 255 / (alpha_max[0] - alpha_min[0]));
                    if (RGBpxl[0] > 255) {
                        RGBpxl[0] = 255;
                    }
                    if (RGBpxl[0] < 0) {
                        RGBpxl[0] = 0;
                    }

                    if (RGBpxl[1] > 255) {
                        RGBpxl[1] = 255;
                    }
                    if (RGBpxl[1] < 0) {
                        RGBpxl[1] = 0;
                    }


                    if (RGBpxl[2] > 255) {
                        RGBpxl[2] = 255;
                    }
                    if (RGBpxl[2] < 0) {
                        RGBpxl[2] = 0;
                    }


                    xx = (int) (i - Math.floor(i / Dim_x) * Dim_x);
                    yy = (int) Math.floor(i / Dim_x);
                    Overlay_ip.putPixel(xx, yy, RGBpxl);

                    if (show_fraction){
                        A1_ip.putPixelValue(xx,yy,alpha[1][i]*count[i]);
                        A2_ip.putPixelValue(xx,yy,alpha[2][i]*count[i]);
                        A3_ip.putPixelValue(xx,yy,alpha[3][i]*count[i]);
                    }

                }
            }

        // Writing files to folder
            if(show_fraction){
                String outpath;
                int num = 1;
                for(ImagePlus AImp: new ImagePlus[]{A1, A2, A3}) {
                    try {
                        outpath = outfolder + "\\A" + num + ".tif";
                        File outFile = new File(outpath);
                        outFile.createNewFile();
                        FileOutputStream out = new FileOutputStream(outFile, false);
                        new TiffEncoder(AImp.getFileInfo()).write(out);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    num++;
                }

            } else {
                int num = 1;
                for(ImagePlus XImp: new ImagePlus[]{X1, X2, X3, Overlay}) {
                    String outpath;
                    try {
                        if (num > 3)
                            outpath = outfolder + "\\Overlay.tif";
                        else
                            outpath = outfolder + "\\X" + num + ".tif";

                        File outputFile = new File(outpath);
                        outputFile.createNewFile();
                        FileOutputStream out = new FileOutputStream(outputFile, false);
                        new TiffEncoder(XImp.getFileInfo()).write(out);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    num++;
                }
            }

        }

}




