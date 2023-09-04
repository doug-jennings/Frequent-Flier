<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");
    
    String pid = request.getParameter("pid");
    String sql = "SELECT pname, total_points FROM Passengers JOIN Point_Accounts ON Passengers.point_account_id = Point_Accounts.point_account_id WHERE passid = ?";

    PreparedStatement stmt=conn.prepareStatement(sql);
    stmt.setString(1, pid);
    ResultSet rs = stmt.executeQuery();

    String output = "";
    while (rs.next()) {
        String pname = rs.getString("pname");
        int total_points = rs.getInt("total_points");
        output += pname + "," + total_points;
    }

    conn.close();
    out.print(output);

%>