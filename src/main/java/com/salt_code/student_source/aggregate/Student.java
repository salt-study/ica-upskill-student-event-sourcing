package com.salt_code.student_source.aggregate;

import com.salt_code.student_source.events.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Getter @Setter
public class Student {
    private UUID id;
    private String fullName;
    private String email;
    private final Set<String> enrolledCourses = new HashSet<>();
    private Instant dateOfBirth;

    public void Apply(StudentCreated studentCreated) {
        id = studentCreated.getStudentID();
        fullName = studentCreated.getFullName();
        email = studentCreated.getEmail();
        dateOfBirth = studentCreated.getDateOfBirth();
    }

    public void Apply(StudentUpdated studentUpdated) {
        fullName = studentUpdated.getFullName();
        email = studentUpdated.getEmail();
    }

    public void Apply(StudentEnrolled enrolled) {
        enrolledCourses.add(enrolled.getCourseName());
    }

    public void Apply(StudentUnEnrolled unEnrolled) {
        enrolledCourses.remove(unEnrolled.getCourseName());
    }

    public void Apply(Event event) {
        switch (event) {
            case StudentCreated studentCreated -> Apply(studentCreated);
            case StudentUpdated studentUpdated -> Apply(studentUpdated);
            case StudentEnrolled enrolled -> Apply(enrolled);
            case StudentUnEnrolled unEnrolled -> Apply(unEnrolled);
            default -> throw new RuntimeException("Unhandled event: " + event);
        }
    }
}
