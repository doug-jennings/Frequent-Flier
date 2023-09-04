<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");
    
    String flightid = request.getParameter("flightid");
    String sql = "SELECT f.*, t.trip_miles, t.trip_id FROM Flights f JOIN flights_trips ft ON f.flight_id = ft.flight_id JOIN Trips t ON ft.trip_id = t.trip_id WHERE f.flight_id = ?";
    PreparedStatement stmt=conn.prepareStatement(sql);
    stmt.setString(1, flightid);
    ResultSet rs = stmt.executeQuery();

    String output = "";
    while (rs.next()) {
        if (!rs.isFirst()) output += "#";
        Date dept_datetime = rs.getDate("dept_datetime");
        Date arrival_datetime = rs.getDate("arrival_datetime");
        int flight_miles = rs.getInt("flight_miles");
        int trip_id = rs.getInt("trip_id");
        int trip_miles = rs.getInt("trip_miles");
        output += dept_datetime + "," + arrival_datetime + "," + flight_miles + "," + trip_id + "," + trip_miles;
    }

    conn.close();
    out.print(output);

%>
