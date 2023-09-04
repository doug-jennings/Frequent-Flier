<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");
    
    String pid = request.getParameter("pid");
    String awardid = request.getParameter("awardid");
    String sql = "SELECT a.a_description, a.points_required, r.redemption_date, ec.center_name FROM Awards a JOIN Redeem r ON a.award_id = r.award_id JOIN Exchgcenters ec ON r.center_id = ec.center_id WHERE r.passid = ? AND a.award_id = ?";

    PreparedStatement stmt=conn.prepareStatement(sql);
    stmt.setString(1, pid);
    stmt.setString(2, awardid);
    ResultSet rs = stmt.executeQuery();

    String output = "";
    while (rs.next()) {
        String a_description = rs.getString("a_description");
        int points_required = rs.getInt("points_required");
        Date redemption_date = rs.getDate("redemption_date");
        String center_name = rs.getString("center_name");
        output += a_description + "," + points_required + "," + redemption_date + "," + center_name;
    }

    conn.close();
    out.print(output);

%>