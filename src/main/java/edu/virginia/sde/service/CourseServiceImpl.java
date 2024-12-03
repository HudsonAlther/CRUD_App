package edu.virginia.sde.service;

import edu.virginia.sde.model.Course;
import edu.virginia.sde.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    // Placeholder for actual database integration
    private final List<Course> courses = new ArrayList<>();

    @Override
    public List<Course> getAllCourses() {
        return courses; // Would be retrieved from the database
    }

    @Override
    public List<Course> searchCourses(String subject, String number, String title) {
        // Simulated search logic - to be replaced with actual database search logic
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            boolean matches = (subject.isEmpty() || course.subjectProperty().get().contains(subject)) &&
                    (number.isEmpty() || String.valueOf(course.numberProperty().get()).contains(number)) &&
                    (title.isEmpty() || course.titleProperty().get().contains(title));
            if (matches) {
                result.add(course);
            }
        }
        return result;
    }

    @Override
    public boolean addCourse(Course course) {
        return courses.add(course); // Would insert into the database in actual implementation
    }
}
