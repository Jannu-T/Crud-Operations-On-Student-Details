import com.google.gson.Gson;
import org.apache.logging.log4j.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//GetAllStudent
@WebServlet("/students")
public class GetAll extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(GetAll.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Establish a database connection using JDBC
        Connection conn=null;
        try {
            Class.forName("org.postgresql.Driver");//Loads the JDBC Driver class
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/StudentDetails","postgres","jannu"); //Connecting to DB
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");
            List<Stu> students = new ArrayList<>(); //Creating list for students

            while (rs.next()) {
                int rollno=rs.getInt("rollno");
                String name = rs.getString("name");
                int age=rs.getInt("age");
                String dept=rs.getString("dept");
                String grade=rs.getString("grade");
                Stu student = new Stu(rollno,name,age,dept,grade);
                // Populate other student attributes if needed
                students.add(student);
            }
            // Convert the list of students to JSON
            String studentsJson = convertStudentsToJson(students);
            // Set the response content type and write the JSON response
            response.setContentType("application/json");
            response.getWriter().write(studentsJson);
            //Log success message
            logger.info("Success");
        } catch (SQLException e) {
            e.printStackTrace();
            //catching error through Logger class
            logger.error("An exception occurred : "+e);
            // Handle database connection errors
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ClassNotFoundException e){
            //Printing the error message
            System.out.println("Driver Error");
            //catching error through Logger class
            logger.error("An exception occurred : "+e);
        } catch (Exception e){
            //catching error through Logger class
            logger.error("An exception occurred : "+e);
        }
    }

    private String convertStudentsToJson (List<Stu> students) {
        // Convert the list of Student objects to JSON using a JSON library
        // Create Gson object
        Gson gson = new Gson();
        // Convert list to JSON
        String json = gson.toJson(students);
        // Return the JSON representation as a string
        return json;
    }
}

