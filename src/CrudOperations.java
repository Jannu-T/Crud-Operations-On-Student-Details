import com.google.gson.Gson;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/student")
public class CrudOperations extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(CrudOperations.class);
    protected void doGet(@org.jetbrains.annotations.NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("rollno"));
        // Establish a database connection using JDBC
        try {
            Class.forName("org.postgresql.Driver"); //Loads the JDBC Driver class
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails", "postgres", "jannu");//Connecting to DB
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student WHERE rollno = ?");//Prepare SQL statement for insertion
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            List<Stu> students = new ArrayList<>();

            //Finding student by using rollno
            if (rs.next()) {
                String name = rs.getString("name");
                int rollno=rs.getInt("rollno");
                int age=rs.getInt("age");
                String dept=rs.getString("dept");
                String grade=rs.getString("grade");

                Stu student = new Stu(rollno,name,age,dept,grade); // Creating a Stu object with fetched student details
                students.add(student);// Adding the student to the list

                // Convert the student object to JSON
                String studentJson = convertStudentsToJson(students);

                // Set the response content type and write the JSON response
                response.setContentType("application/json");
                response.getWriter().write(studentJson);
                //Log success message
                logger.info("Success");
            }
            else {
                // If student not found, return an appropriate response
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                // Printing the response
                response.getWriter().write("Not found!");
                //Log success message
                logger.info("Not found");
            }

            //Closing the statement and connection
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Printing the stack trace of the exception
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Setting the HTTP response status to 500 (Internal Server Error)
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (ClassNotFoundException e){
            System.out.println("Driver error"); // Printing the error message
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (Exception e){
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        }
    }
    protected void doPost(@org.jetbrains.annotations.NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("rollno"));
        String stuName = request.getParameter("name");
        int stuAge = Integer.parseInt(request.getParameter("age"));
        String stuDept = request.getParameter("dept");
        String grade = request.getParameter("grade");

        // Establish a database connection using JDBC
        try {
            Class.forName("org.postgresql.Driver");//Load the JDBC Driver class
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails", "postgres", "jannu");//Create a connection to the database
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO student(rollno,name,age,dept,grade) VALUES(?,?,?,?,?)");//Prepare SQL statement for insertion
            //Set the parameter values in the SQL statement
            stmt.setInt(1, studentId);
            stmt.setString(2,stuName);
            stmt.setInt(3,stuAge);
            stmt.setString(4,stuDept);
            stmt.setString(5,grade);

            //Execute the SQL statement
            stmt.executeUpdate();

            //Close the statement and connection
            stmt.close();
            conn.close();

            // Set the response status and content type
            response.setStatus(HttpServletResponse.SC_OK); // Setting the HTTP response status to 200 (OK)
            response.setContentType("application/json"); // Setting the response content type to JSON

            // Write the response message
            response.getWriter().write("INSERTED");

            //Log message
            logger.info("Inserted");

        } catch (SQLException e) {
            e.printStackTrace(); // Printing the stack trace of the exception
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Setting the HTTP response status to 500 (Internal Server Error)
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (ClassNotFoundException e){
            System.out.println("Driver error"); // Printing the error message
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (Exception e){
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int StudentId = Integer.parseInt(request.getParameter("rollno")); // Extracting the student ID from the request

        // Read the new student details from the request body
        String StudentName = request.getParameter("name");
        int StuAge = Integer.parseInt(request.getParameter("age"));
        String StuDept = request.getParameter("dept");
        String grade = request.getParameter("grade");

        // Establish a database connection using JDBC
        try {
            Class.forName("org.postgresql.Driver"); // Loading the JDBC Driver class
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails", "postgres", "jannu");// Creating a database connection
            PreparedStatement stmt = conn.prepareStatement("UPDATE student SET name = ?, age=?, dept=?, grade=? WHERE rollno = ?");// Creating a prepared statement for updating the student's name

            stmt.setString(1, StudentName); // Setting the new student name
            stmt.setInt(2,StuAge); //Setting the new student age
            stmt.setString(3,StuDept); //Setting the new student dept
            stmt.setString(4,grade); //Setting the new student grade
            stmt.setInt(5, StudentId); // Setting the student rollno

            int rowsUpdated = stmt.executeUpdate(); // Executing the update query

            //Closing the statement and connection
            stmt.close();
            conn.close();

            if (rowsUpdated > 0) {
                // Student name updated successfully
                response.setStatus(HttpServletResponse.SC_OK); // Setting the HTTP response status to 200 (OK)
                response.setContentType("application/json"); // Setting the response content type to JSON
                response.getWriter().write("UPDATED"); // Writing "UPDATED" as the response body
                logger.info("Updated");//Log message
            } else {
                // Student not found or no changes made, return an appropriate response
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                //Printing the response
                response.getWriter().write(("Not found!"));
                //Log success message
                logger.info("Not found");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Printing the stack trace of the exception
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Setting the HTTP response status to 500 (Internal Server Error)
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (ClassNotFoundException e){
            System.out.println("Driver error"); // Printing the error message
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (Exception e){
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        }
    }
    protected void doDelete(@org.jetbrains.annotations.NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("rollno")); // Extracting the student ID from the request

        // Establish a database connection using JDBC
        try {
            Class.forName("org.postgresql.Driver"); //Loading the JDBC Driver class
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails", "postgres", "jannu"); // Creating a database connection
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM student WHERE rollno = ?"); // Creating a prepared statement for deleting the student
            stmt.setInt(1, studentId); // Setting the student ID

            int rowsUpdated = stmt.executeUpdate(); // Executing the delete query

            // Closing the statement and connection
            stmt.close();
            conn.close();
            if (rowsUpdated > 0) {
                // Student name updated successfully
                response.setStatus(HttpServletResponse.SC_OK); // Setting the HTTP response status to 200 (OK)
                response.setContentType("application/json"); // Setting the response content type to JSON
                response.getWriter().write("DELETED"); // Writing "DELETED" as the response body
                //Log success message
                logger.info("Deleted");
            }
            else {
                // If student not found, return an appropriate response
                response.getWriter().write("Can't DELETE");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Setting the HTTP response status to 404 (NOT FOUND)
                //Log success message
                logger.info("Not found");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Printing the stack trace of the exception
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Setting the HTTP response status to 500 (Internal Server Error)
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (ClassNotFoundException e){
            System.out.println("Driver error"); // Printing the error message
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        } catch (Exception e){
            logger.error("An exception occurred: " + e); // Logging the exception using log4j
        }
    }
    private String convertStudentsToJson (List<Stu> students) {
        // Convert the list of Student object to JSON using a JSON library
        // Create Gson object
        Gson gson = new Gson();
        // Convert list to JSON
        String json = gson.toJson(students);
        // Return the JSON representation as a string
        return json;
    }
}
