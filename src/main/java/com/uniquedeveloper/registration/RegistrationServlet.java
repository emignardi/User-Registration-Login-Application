package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("pass");
		String passwordRetry = request.getParameter("re_pass");
		String phone = request.getParameter("contact");
		RequestDispatcher dispatcher = null;
		Connection connection = null;
		if (username == null || username.equals("")) {
			request.setAttribute("status", "Invalid Username");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		if (email == null || email.equals("")) {
			request.setAttribute("status", "Invalid Email");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		if (password == null || password.equals("")) {
			request.setAttribute("status", "Invalid Password");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		} else if (!password.equals(passwordRetry)) {
			request.setAttribute("status", "Invalid Confirm Password");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		if (phone == null || phone.equals("")) {
			request.setAttribute("status", "Invalid Phone Number");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		} else if (phone.length() > 10) {
			request.setAttribute("status", "Invalid Phone Number Length");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/signup_login_application", "root", "password");
			PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password, email, phone) VALUES (?,?,?,?)");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setString(4, phone);
			int rows = ps.executeUpdate();
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rows > 0) {
				request.setAttribute("status", "Success");
			} else {
				request.setAttribute("status", "Fail");
			}
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
