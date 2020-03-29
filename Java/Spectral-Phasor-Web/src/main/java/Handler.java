package main.java;

import ij.process.ImageProcessor;


public class Handler {

    public static void main(String[] args) {

        /*
        Command Line args
        0. Path to file that needs to be processed (C:\Users\...)
        1. Path to file that needs to be written after processing
        2. Operation that is being done
            0: Spectral Phasor
            1: Phasor Unmix
            2: Phasor to Image
        3. This applies for unmixing an image - it is the threshold and background required (see below)
            It must be put in the command line with the following format
            -[parameter] [value]
                where [parameter] is either "threshold", "background," or "show_spectrum"
                and [value] corresponds to the value

            EXAMPLE:
            -threshold 42 -background 16.3 -show_spectrum true

        4. Each of the pixels that was chosen for the unmixer and whether or not show fractions has been chosen
            Format
            x1,y1,x2,y2,x3,y3,f
            f is for false, t is for true
           Will then be parsed
        */

        String path = args[0];
        String outpath = args[1];
        int type = Integer.parseInt(args[2]);
        String specifics = args[3];
        String points;

        switch(type) {
            case 0:
                Spectral_Phasor_Console phasor = new Spectral_Phasor_Console();
                ImageProcessor ip = phasor.consoleSetup(path, outpath, specifics, true);
                phasor.run(ip);
                break;
            case 1:
                points = args[4];
                Spectral_Phasor_Console toBeUnmixed = new Spectral_Phasor_Console();
                ImageProcessor imp = toBeUnmixed.consoleSetup(path, outpath, specifics, false);
                toBeUnmixed.run(imp);
                Phasor_Unmix_Console unmixer = new Phasor_Unmix_Console();
                unmixer.consoleSetup(toBeUnmixed.finishedPhasor, points);
            default:
                System.exit(1);
        }


    }
}