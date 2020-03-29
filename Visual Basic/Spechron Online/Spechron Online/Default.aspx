<%@ Page Title="Home Page" Language="VB" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.vb" Inherits="Spechron_Online._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">

    <div class="jumbotron">
        <h1>Spectral Phasor Online Test</h1>
        <p class="lead">Some text</p>
        <p><a href="http://www.asp.net" class="btn btn-primary btn-lg">Learn more &raquo;</a></p>
        <asp:Panel ID="c2" runat="server">
            <script type="text/javascript">
                <!--

                function FindPosition(oElement) {
                    if (typeof (oElement.offsetParent) != "undefined") {
                        for (var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent) {
                            posX += oElement.offsetLeft;
                            posY += oElement.offsetTop;
                        }
                        return [posX, posY];
                    }
                    else {
                        return [oElement.x, oElement.y];
                    }
                }

                function GetCoordinates(e) {
                    var PosX = 0;
                    var PosY = 0;
                    var ImgPos;
                    ImgPos = FindPosition(document.getElementById("MainContent_PhasorPlot"));
                    if (!e) var e = window.event;
                        if (e.pageX || e.pageY) {
                            PosX = e.pageX;
                            PosY = e.pageY;
                        }
                        else if (e.clientX || e.clientY) {
                            PosX = e.clientX + document.body.scrollLeft
                                + document.documentElement.scrollLeft;
                            PosY = e.clientY + document.body.scrollTop
                                + document.documentElement.scrollTop;
                        }
                    PosX = PosX - ImgPos[0];
                    PosY = PosY - ImgPos[1];
                    // var finalpos = PosX.toString + PosY.toString;
                    if (document.getElementById("MainContent_PhasorPlot").src == "") {
                        document.getElementById("MainContent_problems").innerHTML = "upload an image to select points for unmixing";
                    } else if (document.getElementById("MainContent_coords1").innerHTML == "") {
                        document.getElementById("MainContent_coords1").innerHTML = "X" + PosX + "Y" + PosY;
                    } else if (document.getElementById("MainContent_coords2").innerHTML == "") {
                        document.getElementById("MainContent_coords2").innerHTML = "X" + PosX + "Y" + PosY;
                    } else {
                        document.getElementById("MainContent_coords3").innerHTML = "X" + PosX + "Y" + PosY;
                    }
                    
                }
                
                //-->
            </script>
            <h2>Spectral Phasor</h2>
            Upload a File Here
            <br />
            (it must be a .tif stack)<br />
            <asp:FileUpload ID="SpectralPhasorUpload" runat="server" Width="686px" />
            <br />
            Threshold&nbsp;&nbsp;&nbsp; &nbsp;
            <asp:TextBox ID="ThresholdSelect" runat="server" AutoCompleteType="Disabled"></asp:TextBox>
            <br />
            Background&nbsp;&nbsp;
            <asp:TextBox ID="BackgroundSelect" runat="server"></asp:TextBox>
            <br />
            <asp:Button ID="Button1" runat="server" Height="24px" Text="Upload File" Width="224px" />
            <br />
            Selected File: <asp:Label ID="UploadDetails" runat="server">None</asp:Label>
            <br />
            <asp:Label ID="UploadErrors" runat="server"></asp:Label>
            <br />
            <asp:Label ID="Debug" runat="server"></asp:Label>
            <br />
            <asp:Image ID="PhasorPlot" runat="server" Height="460px" Width="460px" />
            <script type="text/javascript">
                <!--
                window.onload = function () {
                    var myImg = document.getElementById("MainContent_PhasorPlot");
                    myImg.onmousedown = GetCoordinates;
                }
                //-->
            </script>
            <br />
            <h2>Phasor Unmix</h2>
            Coordinates 1: <asp:Label ID="coords1" runat="server"></asp:Label>
            <br />
            Coordinates 2: <asp:Label ID="coords2" runat="server"></asp:Label>           
            <br />
            Coordinates 3: <asp:Label ID="coords3" runat="server"></asp:Label>
            <br />
            <asp:CheckBox ID="showfractions" runat="server" /> Show Fractions
            <asp:Label ID="problems" runat="server"></asp:Label> 
            <br />
            <button type="button" id="SelectionButton" onClick="unmix()" >Select Coordinates to Unmix</button>
            
            <script type="text/javascript">
                function unmix() {
                    if (document.getElementById("MainContent_coords3").innerHTML == "" || document.getElementById("MainContent_PhasorPlot").src == "") {
                        document.getElementById("MainContent_problems").innerHTML = "upload image and select all 3 coordinates first";
                        return;
                    } else {
                        var parameters = "first=" + document.getElementById("MainContent_coords1").innerHTML +
                            "&second=" + document.getElementById("MainContent_coords2").innerHTML +
                            "&third=" + document.getElementById("MainContent_coords3").innerHTML
                            + "&imgsrc=" + document.getElementById("MainContent_UploadDetails").innerHTML;
                        var checked;
                        if (document.querySelector('#MainContent_showfractions').checked)
                            checked = "true";
                        else 
                            checked = "false"

                        window.location.href = "UnmixedImage.aspx?" + parameters + "&showfrac=" + checked;
                    }
                    
                };
            </script>

               
        </asp:Panel>
    </div>

    <div class="row">
        <div class="col-md-4">
            <h2>Getting Started</h2>        <p>
                ASP.NET Web Forms lets you build dynamic websites using a familiar drag-and-drop, event-driven model.
            A design surface and hundreds of controls and components let you rapidly build sophisticated, powerful UI-driven sites with data access.
            </p>
            <p>
                <a class="btn btn-default" href="https://go.microsoft.com/fwlink/?LinkId=301948">Learn more &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Get more libraries</h2>
            <p>
                NuGet is a free Visual Studio extension that makes it easy to add, remove, and update libraries and tools in Visual Studio projects.
            </p>
            <p>
                <a class="btn btn-default" href="https://go.microsoft.com/fwlink/?LinkId=301949">Learn more &raquo;</a>
            </p>
        </div>
        <div class="col-md-4">
            <h2>Web Hosting</h2>
            <p>
                You can easily find a web hosting company that offers the right mix of features and price for your applications.
            </p>
            <p>
                <a class="btn btn-default" href="https://go.microsoft.com/fwlink/?LinkId=301950">Learn more &raquo;</a>
            </p>
        </div>
    </div>

    <script type="text/javascript">
        Private Function ShowImage() As[function]
            Dim f = document.getElementById("SpectralPhasorUpload")
            Dim img = document.getElementById("PhasorPlot")

            img.src = f.value
        End Function
     </script> 
</asp:Content>
