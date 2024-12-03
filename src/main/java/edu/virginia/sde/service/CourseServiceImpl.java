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
        if (subject != null) subject = subject.trim();
        if (number != null) number = number.trim();
        if (title != null) title = title.trim();

        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            String courseSubject = course.getSubject();
            String courseNumber = Integer.toString(course.getNumber());
            String courseTitle = course.getTitle();

            if ((subject == null || subject.isEmpty() || courseSubject.equalsIgnoreCase(subject)) &&
                    (number == null || number.isEmpty() || courseNumber.equalsIgnoreCase(number)) &&
                    (title == null || title.isEmpty() || courseTitle.toLowerCase().contains(title.toLowerCase()))) {
                results.add(course);
            }
        }
        return results;
    }

    private boolean matchesSubject(Course course, String subject) {
        return subject == null || course.getSubject().equalsIgnoreCase(subject);
    }

    private boolean matchesNumber(Course course, String number) {
        return number == null || Integer.toString(course.getNumber()).equalsIgnoreCase(number);
    }

    private boolean matchesTitle(Course course, String title) {
        return title == null || course.getTitle().toLowerCase().contains(title.toLowerCase());
    }



    @Override
    public boolean addCourse(Course course) {
        Course trimmedCourse = new Course(
                course.getSubject().trim(),
                course.getNumber(),
                course.getTitle().trim(),
                course.getAverageRating()
        );
        if (courses.contains(trimmedCourse)) {
            return false;
        }
        courses.add(trimmedCourse);
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
