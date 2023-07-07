import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDetails {
    // Query to get all student
    static final String GET_STUDENTS= "SELECT * FROM student";
    // Query to get a student by rollno
    static final String GET_STUDENT_BY_ROLLNO = "SELECT * FROM student WHERE rollno = ?";

    // Query to insert a student
    static final String INSERT_STUDENT = "INSERT INTO student(rollno, name, age, dept, grade) VALUES(?, ?, ?, ?, ?)";

    // Query to update a student
    static final String UPDATE_STUDENT = "UPDATE student SET name = ?, age = ?, dept = ?, grade = ? WHERE rollno = ?";

    // Query to delete a student
    static final String DELETE_STUDENT = "DELETE FROM student WHERE rollno = ?";

    //Gson object
    public static final Gson gson=new Gson();
    int rollno;
    String name;
    int age;
    String grade;
    String dept;
    StudentDetails(int rollno,String name,int age,String dept,String grade)
    {
        this.rollno=rollno;
        this.name=name;
        this.age=age;
        this.dept=dept;
        this.grade=grade;
    }
    //Extracting the values from request body
    public static StudentDetails studentFromRequest(HttpServletRequest request) {
        int studentId = rollNoFromRequest(request);
        String stuName = request.getParameter("name");
        int stuAge = Integer.parseInt(request.getParameter("age"));
        String stuDept = request.getParameter("dept");
        String stuGrade = request.getParameter("grade");

        // Create a new StudentDetails object with the extracted values
        return new StudentDetails(studentId, stuName, stuAge, stuDept, stuGrade);
    }
    //Extracting the value of rollno from request body
    public static int rollNoFromRequest(HttpServletRequest request)
    {
        return Integer.parseInt(request.getParameter("rollno"));
    }
}
