<%@page import="java.sql.*" %>

<%
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    Connection conn = DriverManager.getConnection(url, "djennin5", "xutchoph");

    String spid = request.getParameter("spid");
    String dpid = request.getParameter("dpid");
    int npoints = Integer.parseInt(request.getParameter("npoints"));

    // Check if pids exist and spid has enough points
    String checkSql = "SELECT p.passid, pa.total_points FROM Passengers p JOIN Point_Accounts pa ON p.point_account_id = pa.point_account_id WHERE p.passid IN (?, ?)";
    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
    checkStmt.setString(1, spid);
    checkStmt.setString(2, dpid);
    ResultSet rs = checkStmt.executeQuery();

    String output = "";
    boolean spidExists = false;
    boolean dpidExists = false;
    int spidPoints = 0;

    while (rs.next()) {
        String pid = rs.getString(1);
        if (pid.equals(spid)) {
            spidExists = true;
            spidPoints = rs.getInt(2);
        } else if (pid.equals(dpid)) {
            dpidExists = true;
        }
    }

    if (!spidExists || !dpidExists) {
        output = "Invalid passenger IDs.";
    } else if (npoints > spidPoints) {
        output = "Not enough points to transfer.";
    } else {
        // Deduct points from spid
        String deductPointsSql = "UPDATE Point_Accounts pa SET pa.total_points = pa.total_points - ? WHERE pa.point_account_id IN (SELECT p.point_account_id FROM Passengers p WHERE p.passid = ?)";
        PreparedStatement deductStmt = conn.prepareStatement(deductPointsSql);
        deductStmt.setInt(1, npoints);
        deductStmt.setString(2, spid);
        int rowsUpdated = deductStmt.executeUpdate();

        // Add points to dpid
        String addPointsSql = "UPDATE Point_Accounts pa SET pa.total_points = pa.total_points + ? WHERE pa.point_account_id IN (SELECT p.point_account_id FROM Passengers p WHERE p.passid = ?)";
        PreparedStatement addStmt = conn.prepareStatement(addPointsSql);
        addStmt.setInt(1, npoints);
        addStmt.setString(2, dpid);
        rowsUpdated += addStmt.executeUpdate();

        if (rowsUpdated > 0) {
            output = "Transfer successful.";
        } else {
            output = "Transfer failed.";
        }
    }

    conn.close();
    out.print(output);

%>
