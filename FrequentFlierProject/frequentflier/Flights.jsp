<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");
    
    String pid = request.getParameter("pid");
    String sql = "SELECT f.flight_id, f.flight_miles, f.destination FROM passengers p JOIN flights f ON p.passid = f.passid WHERE p.passid = ?";

    PreparedStatement stmt=conn.prepareStatement(sql);
    stmt.setString(1, pid);
    ResultSet rs = stmt.executeQuery();

    String output = "";
    while (rs.next()) {
        if (!rs.isFirst()) output += "#";
        int flight_id = rs.getInt("flight_id");
        int flight_miles = rs.getInt("flight_miles");
        String destination = rs.getString("destination");
        output += flight_id + "," + flight_miles + "," + destination;
    }

    conn.close();
    out.print(output);

%>