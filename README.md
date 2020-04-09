# Spectral Phasor Web Approach

## What this is

This is made to adapt the [ImageJ](https://imagej.nih.gov) plugins made by [Spechron.com](http://spechron/com) so that they are usable as web applications. This way, the user does not have to actually install ImageJ or the Spectral Phasor plugins on their own machine.

## Installation

Most of the VB code is auto-generated. If you already have a website and you want to add this to your website, add the following:
1. Install Java on the server that is running your website
2. On your website's server, add a folder called Config and a folder called UploadedFiles
3. Build a jar (source is in the Java folder) and place it anywhere on the server, but remember where it is.
4. In the Config folder, add a text file called jarfile.txt. In it, place the location of the jar from step 3.
5. Add the following pages to the website:* 
- Default.aspx
- Default.aspx.vb
- Default.aspx.designer.vb (optional)

- UnmixedImage.aspx
- UnmixedImage.aspx.vb
- UnmixedImage.aspx.designer.vb (optional)

*These pages can be renamed to whatever you want them to be when they are on the website. However, they do contain references to each other so just make sure that if you refactor the name of one of them, you check for any mentions of it on any of the other pages and change it's name as well. Currently, Default.aspx is not mentioned by anything though; changing its name should be seamless.

