/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.piyushpatel2005;

import com.piyushpatel2005.util.SQLUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pc
 */
public class SQLServlet extends HttpServlet {


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/test";
            String username = "root";
            String password = "nectar";
            Connection con = DriverManager.getConnection(dbURL, username, password);
            
            Statement statement = con.createStatement();
            
            sqlStatement = sqlStatement.trim();
            if(sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);
                if(sqlType.equalsIgnoreCase("select")) {
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                } else {
                    int i = statement.executeUpdate(sqlStatement);
                    if(i == 0) {
                        sqlResult = "<p>The statement executed successfully.</p>";
                    } else {
                        sqlResult = "<p>The statement executed successfuly.<br>" +
                                    i + "row(s) affected.</p>";
                    }
                }
            }
            statement.close();
            con.close();
            
        }
        catch(ClassNotFoundException e) {
            sqlResult = "<p>Error loading the database driver: <br>" + 
                        e.getMessage() + "</p>";
        }
        catch(SQLException e) {
            sqlResult = "<p>Error executing the SQL statement.<br>" +
                        e.getMessage() + "</p>";
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);
        
        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
