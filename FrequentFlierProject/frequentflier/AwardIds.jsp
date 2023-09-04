<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");
    
    String pid = request.getParameter("pid");
    String sql = "SELECT DISTINCT a.award_id FROM Awards a JOIN Redeem r ON a.award_id = r.award_id WHERE r.passid = ?";

    PreparedStatement stmt=conn.prepareStatement(sql);
    stmt.setString(1, pid);
    ResultSet rs = stmt.executeQuery();

    String output = "";
    while (rs.next()) {
        if (!rs.isFirst()) output += "#";
        int award_id = rs.getInt("award_id");
        output += award_id;
    }

    conn.close();
    out.print(output);

%>