package jpa.mainrunner;
import jpa.entitymodels.Course;
import jpa.entitymodels.Student;

import java.util.List;
import java.util.Scanner;
import jpa.service.CourseService;
import jpa.service.StudentService;

public class Main {

    private final Scanner studentinput;
    private final StringBuilder sb;
    private final CourseService courseService;
    private final StudentService studentService;
    private Student currentStudent;

    public Main() {
        studentinput = new Scanner(System.in);
        sb = new StringBuilder();
        courseService = new CourseService();
        studentService = new StudentService();
    }

    // Set parameters

    public static void main(String[] args) {
        StudentService studentService = new StudentService();
        boolean student = studentService.validateStudent("aiannitti7@is.gd","TWP4hf5j");
        System.out.println("students");
        System.out.println(student);


        Main sms = new Main();
        sms.run();
    }

    private void run() {
        // Login or quit
        switch (menu1()) {
            case 1:
                if (studentLogin()) {
                    registerMenu();
                }
                break;
            case 2:
                System.out.println("Try again");
                break;

            default:

        }
    }

    private int menu1() {
        sb.append("\n1.Student Login\n2. Quit\nPlease Enter 1 or 2: ");
        System.out.println(sb.toString());
        sb.delete(0, sb.length());

        return studentinput.nextInt();
    }

    private boolean studentLogin() {
        boolean retValue = false;
        System.out.println("Enter your email address: ");
        String email = studentinput.next();
        System.out.println("Enter your password: ");
        String password = studentinput.next();

        

        Student students = studentService.getStudentByEmail(email);
       // List<Student> students = studentService.getAllStudents();
        if (students != null) {
            currentStudent = students;
            
          
        }

       // assert currentStudent != null;
        if (currentStudent != null && currentStudent.getsPass().equals(password)) {
            List<Course> courses = studentService.getStudentCourses(email);
         
            System.out.println("MyClasses");
            for (Course course : courses) {
               
            }
            retValue = true;
        } else {
            System.out.println("User Validation failed. Try again!! ");
        }
        return retValue;
    }

    private void registerMenu() {
        sb.append("1. Register to Class" + "\n" + "2. Logout " + "Please enter 1 or 2");
        System.out.println(sb.toString());
        sb.delete(0, sb.length());

        switch (studentinput.nextInt()) {
            case 1:
                List<Course> allCourses = courseService.getAllCourses();
                List<Course> studentCourses = studentService.getStudentCourses(currentStudent.getsEmail());
                allCourses.removeAll(studentCourses);
                System.out.printf("%5s%15S%15s\n", "ID", "Course", "Instructor");
               
                for (Course course : allCourses) {
                    course.print();
                  
                }
                System.out.println();
                System.out.println("Which Course?");
                int number = studentinput.nextInt();
                Course newCourse = courseService.getAllCourses().get(number-1);

                if (newCourse != null) {
                    studentService.registerStudentToCourse(currentStudent.getsEmail(), newCourse.getcId());
                    Student temp = studentService.getStudentByEmail(currentStudent.getsEmail());

                    StudentService scService = new StudentService();
                    List<Course> sCourses = scService.getStudentCourses(temp.getsEmail());


                    System.out.println("MyClasses");
                    for (Course course : sCourses) {
                        course.print();
                        
                    }
                }
                break;
            case 2:
            default:
                System.out.println("You have been signed out.");
        }
    }
}
