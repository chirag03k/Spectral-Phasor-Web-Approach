<%@ Page Language="vb" AutoEventWireup="false" CodeBehind="UnmixedImage.aspx.vb" Inherits="Spechron_Online.WebForm1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <asp:Label ID="Content" runat="server" Text=""></asp:Label>
        </div>
        <div>
            <asp:Image ID="Output1" runat="server" Height="500px" Width="500px" />
        </div>
        <div>
            <asp:Image ID="Output2" runat="server" Height="500px" Width="500px" />
        </div>
        <div>
            <asp:Image ID="Output3" runat="server" Height="500px" Width="500px" />
        </div>
        <div>
            <asp:Image ID="Overlay" runat="server" Height="500px" Width="500px" />
        </div>
    </form>
</body>
</html>
