package jpa.service;

import jpa.dao.StudentDAO;
import jpa.entitymodels.Course;
import jpa.entitymodels.Student;


import javax.persistence.*;
import java.util.List;


public class StudentService implements StudentDAO {

    private EntityManagerFactory emf;
    private EntityManager em;

    public StudentService() {
        
        emf = Persistence.createEntityManagerFactory("SMS");
        em = emf.createEntityManager();
    }
    @Override
    public List<Student> getAllStudents(){

        try {
            String sql = "SELECT s FROM student s";

            // set this to use the correct entity
            TypedQuery<Student> query = em.createQuery(sql, Student.class);


            List<Student> students = query.getResultList();

            return students;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public Student getStudentByEmail(String email){
        try {
            String sql = "SELECT s FROM student s where email = :email";

            // set this to use the correct entity
            TypedQuery<Student> query = em.createQuery(sql, Student.class);
            query.setParameter("email", email);

            Student student = query.getSingleResult();

            return student;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public boolean validateStudent(String email, String password){

        try {
            String sql = "SELECT s FROM student s where email = :email AND password = :password";

            // set this to use the correct entity
            TypedQuery<Student> query = em.createQuery(sql, Student.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            Student student = query.getSingleResult();
            if (student != null){
                return true;

            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }
    @Override
    public void registerStudentToCourse(String sEmail, int cId){
        try {

            boolean isInCourse = false;
            List<Course> courses = getStudentCourses(sEmail);
            for(Course course : courses){
                if(course.getcId() == cId){
                    isInCourse = true;
                }
            }

            if (!isInCourse){
                String sqlSave = "INSERT INTO student_course (sEmail, cId) VALUES (:student_email, :sCourses_id)";
                em.getTransaction().begin();
                em.createNativeQuery(sqlSave)
                        .setParameter("student_email", sEmail)
                        .setParameter("sCourses_id", cId)
                        .executeUpdate();
                em.getTransaction().commit();

               
            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }
//   
    @Override
    public List<Course> getStudentCourses(String sEmail) {
        Student student = getStudentByEmail(sEmail);
        return student.getsCourses();
    }


}
