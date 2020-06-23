using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace ImageManipulation
{
    class Spectral_Phasor
    {

        public void GenerateGraph(Bitmap[] imgStack, double P_Threshold, double P_Background, Boolean P_ShowSpectrum)
        {

            int Dimensions = imgStack[0].Width * imgStack[0].Height;

            double Threshold = P_Threshold;
            double Background = P_Background;
            Boolean ShowSpectrum = P_ShowSpectrum;

            int Phasor_Dim = 400;
            int ShiftX = 28;
            int ShiftY = 10;

            int M = 0;
            int K = 3; // This is the number of "slices" 

            // Histogram min/max
            double H_Min = 255 * 255 * 255;
            double H_Max = 0;

            //Maximum count number
            double Max_Count = 0;
            double Min_Count = 1e10;

            // Image Dimensions
            int Dim_X = imgStack[0].Width;
            int Dim_Y = imgStack[0].Height;

            // Real and imaginary parts of specs over Image
            double[,] S_Real = new double[Dim_X, Dim_Y];
            double[,] S_Im = new double[Dim_X, Dim_Y];
            double C_X, C_Y;

            // Real and imaginary parts of crescent
            double[] C_Real = new double[10000];
            double[] C_Im = new double[10000];

            // Total count of every pixel
            double[,] Count = new double[Dim_X, Dim_Y];
            double[] Count_Im = new double[Dim_X * Dim_Y];

            // First harmonic frequency
            double Omega = 2.0 * Math.PI / K;

            // Spec Curve at Every Pixel
            double[,,] Spec = new double[K + 1, Dim_X, Dim_Y];
            double[] Spec_T = new double[K + 1];
            double[] Spec_TX = new double[K + 1];

            // Phasor of each pixel
            double[] PhasorMap_Real = new double[Dim_X * Dim_Y];
            double[] PhasorMap_Im = new double[Dim_X * Dim_Y];
            double V = 0;
            double U = 0;

            // Build the spec curve for each pixel
            for (int Y = 0; Y < Dim_Y; Y++)
            {
                for (int X = 0; X < Dim_X; X++)
                {
                    for (int I = 0; I < K; I++)
                    {
                        // TODO: what is ROI? How does it work?

                        Color pixel = imgStack[I].GetPixel(X, Y);
                        
                    }
                }
            }
        }

    }
}
