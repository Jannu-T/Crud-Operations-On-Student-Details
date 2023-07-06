import com.google.gson.Gson;
<<<<<<< HEAD
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
=======
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.NotNull;
>>>>>>> e6dda74 (first commit)

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.jetbrains.annotations.NotNull;
=======
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
>>>>>>> e6dda74 (first commit)

@WebServlet("/student")
public class CrudOperations extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(CrudOperations.class);
<<<<<<< HEAD
    //Creating Gson object
    public static final Gson gson=new Gson();

    // Establish a database connection using JDBC
    Connection conn;
    private void connection(){
        try {
            //Loads the JDBC Driver class
            Class.forName("org.postgresql.Driver");
            //Connecting to DB
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails", "postgres", "jannu");
            //Log message for successful DB connection
            logger.info("StudentDetails database successfully connected");
        } catch (ClassNotFoundException e){
            // Printing the Driver error message
            System.out.println("Driver error");
            logger.error("An exception occurred: " + e);
        } catch (SQLException e) {
            // Printing the error message
            System.out.println("Database access error");
            logger.error("An exception occurred: " + e);
        } catch (Exception e) {
            //Error message
            logger.error("An error occurred in database connection : "+ e);
        }
    }
    //Read operation in CRUD operations
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestParameter=request.getParameter("rollno");

        connection();
        //Creating list for students
        List<StudentDetails> students = new ArrayList<>();
        //Setting the content type of response as JSON
        response.setContentType("application/json");
        try {
            if (requestParameter == null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(StudentDetails.GET_STUDENTS);

                while (rs.next()) {
                    StudentDetails student = StudentDetails.getData(rs);
                    // Populate other student attributes if needed
                    students.add(student);
                }

                // Convert the list of students to JSON
                String studentsJson = gson.toJson(students);//convertStudentsToJson(students);

                // Writing the JSON response
                response.getWriter().write(studentsJson);

                //Log success message
                logger.info("GetAllStudent operation is successfully done.");
            }
            else {
                int studentId = Integer.parseInt(requestParameter);
                //Prepare SQL statement for insertion
                PreparedStatement stmt = conn.prepareStatement(StudentDetails.GET_STUDENT_BY_ROLLNO);

                stmt.setInt(1, studentId);
                ResultSet rs = stmt.executeQuery();

                //Finding student by using rollno
                if (rs.next()) {

                    StudentDetails student = StudentDetails.getData(rs);
                    // Adding the student to the list
                    students.add(student);

                    // Convert the student object to JSON
                    String studentJson = gson.toJson(students);

                    // Writing the JSON response
                    response.getWriter().write(studentJson);

                    //Log success message
                    logger.info("GetByStudentId operation is successfully done");
                }
                else {
                    // If student not found, return an appropriate response
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    // Printing the response
                    response.getWriter().write(gson.toJson("Rollno not found. Can't get."));
                    //Log success message
                    logger.info("Rollno not found. Can't get.");
                }

                //Closing the statement and connection
                stmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in GET method (SQLException) : " + e));
            logger.error("An exception occurred in GET method (SQLException) : " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in GET method : " + e));
            logger.error("An exception occurred in GET method : " + e);
        }
    }
    protected void doPost(@org.jetbrains.annotations.NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        //Extracting the values from request body
        StudentDetails studentDetails=StudentDetails.studentFromRequest(request);
        //Setting the content type of response as JSON
        response.setContentType("application/json");

        try {
            connection();

            //Prepare SQL statement for insertion
            PreparedStatement stmt = conn.prepareStatement(StudentDetails.INSERT_STUDENT);

            //Set the parameter values in the SQL statement
            stmt.setInt(1, studentDetails.rollno);
            stmt.setString(2,studentDetails.name);
            stmt.setInt(3,studentDetails.age);
            stmt.setString(4,studentDetails.dept);
            stmt.setString(5,studentDetails.grade);
=======
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
>>>>>>> e6dda74 (first commit)

            //Execute the SQL statement
            stmt.executeUpdate();

            //Close the statement and connection
            stmt.close();
            conn.close();

<<<<<<< HEAD
            // Set the response status
            response.setStatus(HttpServletResponse.SC_OK);

            // Write the response message
            response.getWriter().write(gson.toJson("INSERTED SUCCESSFULLY"));

            //Log message
            logger.info("Student record inserted successfully.");

        } catch (SQLException e) {
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in POST method (SQLException) : " + e));
            logger.error("An exception occurred in POST method (SQLException) : " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in POST method : " + e));
            logger.error("An exception occurred in POST method : " + e);
=======
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
>>>>>>> e6dda74 (first commit)
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
<<<<<<< HEAD
        //Extracting the values from request body
        StudentDetails studentDetails=StudentDetails.studentFromRequest(request);
        //Set the content type of response as JSON
        response.setContentType("application/json");

        try {
            connection();

            // Creating a prepared statement for updating the student's name
            PreparedStatement stmt = conn.prepareStatement(StudentDetails.UPDATE_STUDENT);

            //Setting the new data to the student
            stmt.setString(1,studentDetails.name);
            stmt.setInt(2,studentDetails.age);
            stmt.setString(3,studentDetails.dept);
            stmt.setString(4,studentDetails.grade);
            stmt.setInt(5, studentDetails.rollno);

            // Executing the update query
            int rowsUpdated = stmt.executeUpdate();
=======
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
>>>>>>> e6dda74 (first commit)

            //Closing the statement and connection
            stmt.close();
            conn.close();

            if (rowsUpdated > 0) {
                // Student name updated successfully
<<<<<<< HEAD
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson("UPDATED SUCCESSFULLY"));
                logger.info("Student record successfully updated.");//Log message
=======
                response.setStatus(HttpServletResponse.SC_OK); // Setting the HTTP response status to 200 (OK)
                response.setContentType("application/json"); // Setting the response content type to JSON
                response.getWriter().write("UPDATED"); // Writing "UPDATED" as the response body
                logger.info("Updated");//Log message
>>>>>>> e6dda74 (first commit)
            } else {
                // Student not found or no changes made, return an appropriate response
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                //Printing the response
<<<<<<< HEAD
                response.getWriter().write(gson.toJson("Rollno not found. Can't update."));
                //Log success message
                logger.info("Rollno not found. Can't update.");
            }
        } catch (SQLException e) {
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in PUT method (SQLException) : " + e));
            logger.error("An exception occurred in PUT method (SQLException) : " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in PUT method : " + e));
            logger.error("An exception occurred in PUT method : " + e);
        }
    }
    protected void doDelete(@org.jetbrains.annotations.NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        // Extracting the student ID from the request
        int studentId = StudentDetails.rollNoFromRequest(request);
        //Set the content type of response as JSON
        response.setContentType("application/json");

        try {
            connection();

            // Creating a prepared statement for deleting the student
            PreparedStatement stmt = conn.prepareStatement(StudentDetails.DELETE_STUDENT);
            stmt.setInt(1, studentId);

            // Executing the delete query
            int rowsUpdated = stmt.executeUpdate();
=======
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
>>>>>>> e6dda74 (first commit)

            // Closing the statement and connection
            stmt.close();
            conn.close();
<<<<<<< HEAD

            if (rowsUpdated > 0) {
                // Student name updated successfully
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson("DELETED SUCCESSFULLY"));
                //Log success message
                logger.info("Student record successfully deleted.");
            }
            else {
                // If student not found, return an appropriate response
                response.getWriter().write(gson.toJson("Rollno not found. Can't Delete"));
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                //Log success message
                logger.info("Rollno not found. Can't Delete");
            }
        } catch (SQLException e) {
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in DELETE method (SQLException) : " + e));
            logger.error("An exception occurred in DELETE method (SQLException) : " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            // Printing the error message as response
            response.getWriter().write(gson.toJson("An exception occurred in DELETE method : " + e));
            logger.error("An exception occurred in DELETE method : " + e);
        }
    }
}
=======
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
>>>>>>> e6dda74 (first commit)
