/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/10.0.23
 * Generated at: 2023-06-23 19:16:57 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import java.sql.*;

public final class TransferPoints_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final jakarta.servlet.jsp.JspFactory _jspxFactory =
          jakarta.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("java.sql");
    _jspx_imports_packages.add("jakarta.servlet");
    _jspx_imports_packages.add("jakarta.servlet.http");
    _jspx_imports_packages.add("jakarta.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile jakarta.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public jakarta.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final jakarta.servlet.http.HttpServletRequest request, final jakarta.servlet.http.HttpServletResponse response)
      throws java.io.IOException, jakarta.servlet.ServletException {

    if (!jakarta.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final jakarta.servlet.jsp.PageContext pageContext;
    jakarta.servlet.http.HttpSession session = null;
    final jakarta.servlet.ServletContext application;
    final jakarta.servlet.ServletConfig config;
    jakarta.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    jakarta.servlet.jsp.JspWriter _jspx_out = null;
    jakarta.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");

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


      out.write('\r');
      out.write('\n');
    } catch (java.lang.Throwable t) {
      if (!(t instanceof jakarta.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
