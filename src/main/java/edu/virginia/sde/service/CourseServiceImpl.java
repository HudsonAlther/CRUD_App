package edu.virginia.sde.service;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final List<Course> courses = new ArrayList<>();

    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    @Override
    public List<Course> searchCourses(String subject, String number, String title) {
        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if ((subject == null || course.subjectProperty().get().equalsIgnoreCase(subject)) &&
                    (number == null || Integer.toString(course.numberProperty().get()).equalsIgnoreCase(number)) &&
                    (title == null || course.titleProperty().get().toLowerCase().contains(title.toLowerCase()))) {
                results.add(course);
            }
        }
        return results;
    }

    @Override
    public boolean addCourse(Course course) {
        if (courses.contains(course)) {
            return false;
        }
        courses.add(course);
        return true;
    }

    @Override
    public Course getCourseById(int courseId) {
        for (Course course : courses) {
            if (course.numberProperty().get() == courseId) {
                return course;
            }
        }
        return null;
    }
}
