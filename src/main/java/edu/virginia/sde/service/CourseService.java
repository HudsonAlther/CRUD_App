package edu.virginia.sde.service;

import edu.virginia.sde.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    List<Course> searchCourses(String subject, String number, String title);
    boolean addCourse(Course course);
    Course getCourseById(int courseId);
}
