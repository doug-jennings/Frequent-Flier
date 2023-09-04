/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Douglas
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class Login extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        PrintWriter out = response.getWriter();

        try {
            // Load JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            // Create database connection
            String connString = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
            Connection conn = DriverManager.getConnection(connString, "djennin5", "xutchoph");
            // Create statement object
            String sql = "SELECT passid FROM LOGIN WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                // Execute statement
                try (ResultSet rs = stmt.executeQuery()) {
                    // Process resultset
                    if (rs.next()) {
                        String pid = rs.getString("passid");
                        out.print("Yes:" + pid);
                    } else {
                        out.print("No");
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }
}