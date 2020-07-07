Imports System.Drawing
Imports System.Drawing.Imaging
Imports System.IO
Public Class _Default
    Inherits Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As EventArgs) Handles Me.Load
        SpectralPhasorUpload.Attributes.Add("onFocus", "ShowImage()")
    End Sub

    Protected Sub Button1_Click(sender As Object, e As EventArgs) Handles Button1.Click
        If SpectralPhasorUpload.HasFile = False Then
            UploadDetails.Text = "Choose a File first"
        ElseIf SpectralPhasorUpload.FileName.Contains(".tif") Then
            UploadDetails.Text = SpectralPhasorUpload.FileName
            Dim path As String = (Server.MapPath("UploadedFiles") & "\") + SpectralPhasorUpload.FileName
            SpectralPhasorUpload.SaveAs(path)



            Dim Threshold As Double
            Dim Background As Double
            Try
                Threshold = ThresholdSelect.Text
            Catch ex As Exception
                ThresholdSelect.Text = 50
                Threshold = 50
                UploadErrors.Text = UploadErrors.Text & vbNewLine & "Invalid value for Threshold. Using 50"
            End Try

            Try
                Background = BackgroundSelect.Text
            Catch ex As Exception
                BackgroundSelect.Text = 0
                Background = 0
                UploadErrors.Text = UploadErrors.Text & vbNewLine & "Invalid value for Background. Using 0"
            End Try


            Dim W, H, D As Integer
            Dim img()(,) As Single

            Dim I As Integer
            'Reads the uploaded tiff file and converts it into a jagged array 
            ReadMultiJaggedArray(path, img, W, H, D)
            Dim Bmp As New Bitmap(W, H, Imaging.PixelFormat.Format24bppRgb)
            ' A preliminary way of converting the array into a bitmap where the total intensity is demostrated.
            For y = 0 To H - 1
                For x = 0 To W - 1
                    I = 0
                    For z = 0 To D - 1
                        I += img(z)(x, y) / D
                        Bmp.SetPixel(x, y, Color.FromArgb(255, I, I, I))
                    Next
                Next
            Next
            'Converting the bitmap into memory stream. This way you don't need to save theimage and then display it on the imgctrl
            Dim ms As MemoryStream = New MemoryStream()
            Bmp.Save(ms, ImageFormat.Png)
            Dim base64Data = Convert.ToBase64String(ms.ToArray())
            Image1.ImageUrl = "data:image/png;base64," + base64Data




            'Dim args As String = "-threshold " + Threshold.ToString + " -background " + Background.ToString

            'Dim jarText As String
            'jarText = Server.MapPath("Config") & "\jarfile.txt"
            'Dim jarPath As String = My.Computer.FileSystem.ReadAllText(jarText)


            'Dim cmd As String = "java -jar " + """" + jarPath + """" +
            '            " """ + path + """ " +
            '            """" + path.Replace(".", "-") + "plot.tif" + """ " +
            '            """0"" " +
            '            """" + args + """"

            'Shell(cmd)
            'Threading.Thread.Sleep(1500) ' 500 milliseconds = 0.5 seconds


            'PhasorPlot.ImageUrl = "UploadedFiles\" + UploadDetails.Text.Replace(".", "-") + "plot.tif"

        Else
            UploadDetails.Text = "Must be a tif file"
        End If
    End Sub
End Class