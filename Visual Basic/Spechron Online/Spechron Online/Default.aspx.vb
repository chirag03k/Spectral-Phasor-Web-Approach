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
                Threshold = 50
                UploadErrors.Text = UploadErrors.Text & vbNewLine & "Invalid value for Threshold. Using 50"
            End Try

            Try
                Background = BackgroundSelect.Text
            Catch ex As Exception
                Background = 0
                UploadErrors.Text = UploadErrors.Text & vbNewLine & "Invalid value for Background. Using 0"
            End Try

            Dim args As String = "-threshold " + Threshold.ToString + "-background " + Background.ToString

            Dim cmd As String = "java -jar C:/Users/micro/IdeaProjects/Spectral-Phasor-Web/out/artifacts/Spectral_Phasor_Web_jar/Spectral-Phasor-Web.jar " +
                        """" + path + """ " +
                        """" + path.Replace(".", "-") + "plot.tif" + """ " +
                        """Spectral_Phasor"" " +
                        """" + args + """"
            Shell(cmd)
            Debug.Text = "Selected Threshold: " + Threshold.ToString + ". Selected Background: " + Background.ToString + "."
            PhasorPlot.ImageUrl = "UploadedFiles\" + SpectralPhasorUpload.FileName.Replace(".", "-") + "plot.tif"
            ' My.Computer.FileSystem.DeleteFile(path)
        Else
            UploadDetails.Text = "Must be a tif file"
        End If
    End Sub


End Class