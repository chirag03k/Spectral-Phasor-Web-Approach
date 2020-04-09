Imports System.Drawing
Public Class WebForm1
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load

        Dim firstString As String = Request.QueryString("first")
        Dim firstY As Integer = Integer.Parse(Split(firstString, "Y")(1))
        Dim firstX As Integer = Split(Split(firstString, "X")(1), "Y")(0)

        Dim secondString As String = Request.QueryString("second")
        Dim secondY As Integer = Integer.Parse(Split(secondString, "Y")(1))
        Dim secondX As Integer = Split(Split(secondString, "X")(1), "Y")(0)

        Dim thirdString As String = Request.QueryString("third")
        Dim thirdY As Integer = Integer.Parse(Split(thirdString, "Y")(1))
        Dim thirdX As Integer = Split(Split(thirdString, "X")(1), "Y")(0)

        Dim imgsrc As String = HttpUtility.UrlDecode(Request.QueryString("imgsrc"))
        Dim path As String = (Server.MapPath("UploadedFiles") & "\") + imgsrc
        My.Computer.FileSystem.CreateDirectory(path.Replace(".", "-") + "outputs")

        Dim showFractions As Boolean = Boolean.Parse(Request.QueryString("showfrac"))
        Dim txtsf As String = "f"
        If showFractions Then
            txtsf = "t"
        End If

        Dim threshold As String = Request.QueryString("t")
        Dim background As String = Request.QueryString("b")

        Dim args As String = "-threshold " + threshold + " -background " + background

        Dim jarText As String
        jarText = Server.MapPath("Config") & "\jarfile.txt"
        Dim jarPath As String = My.Computer.FileSystem.ReadAllText(jarText)

        Dim points As String
        points = firstX.ToString + "," + firstY.ToString + "," + secondX.ToString + "," + secondY.ToString + "," + thirdX.ToString + "," + thirdY.ToString + "," + txtsf

        Dim cmd As String = "java -jar " + " """ + jarPath + """" +
                    " """ + path + """ " +
                    """" + path.Replace(".", "-") + "outputs" + """ " +
                    """1"" " +
                    """" + args + """" + " """ + points + """"
        Shell(cmd)
        Threading.Thread.Sleep(1500) ' 500 milliseconds = 0.5 seconds
        Content.Text = cmd
        If showFractions Then

            Output1.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "A1.tif"
            Dim fbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "A1.tif")
            Output1.Height = fbmp.Height
            Output1.Width = fbmp.Width

            Output2.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "A2.tif"
            Dim sbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "A2.tif")
            Output2.Height = sbmp.Height
            Output2.Width = sbmp.Width

            Output3.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "A3.tif"
            Dim tbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "A3.tif")
            Output3.Height = tbmp.Height
            Output3.Width = tbmp.Width

        Else

            Output1.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "X1.tif"
            Dim fbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "X1.tif")
            Output1.Height = fbmp.Height
            Output1.Width = fbmp.Width

            Output2.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "X2.tif"
            Dim sbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "X2.tif")
            Output2.Height = sbmp.Height
            Output2.Width = sbmp.Width

            Output3.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "X3.tif"
            Dim tbmp As New Bitmap(path.Replace(".", "-") + "outputs\" + "X3.tif")
            Output3.Height = tbmp.Height
            Output3.Width = tbmp.Width

            Overlay.ImageUrl = "UploadedFiles\" + imgsrc.Replace(".", "-") + "outputs\" + "Overlay.tif"
            Dim bmpO As New Bitmap(path.Replace(".", "-") + "outputs\" + "Overlay.tif")
            Overlay.Height = bmpO.Height
            Overlay.Width = bmpO.Width

        End If

    End Sub

End Class