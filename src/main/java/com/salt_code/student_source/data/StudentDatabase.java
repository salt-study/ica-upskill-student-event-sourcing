package com.salt_code.student_source.data;

import com.salt_code.student_source.aggregate.Student;
import com.salt_code.student_source.events.Event;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentDatabase {

    private final HashMap<UUID, List<Event>> studentEvents = new HashMap<>();
    private final HashMap<UUID, Student> students = new HashMap<>();

    public int StreamsCount() {
        return studentEvents.size();
    }

    public int StreamLength(UUID id) {
        return Optional.ofNullable(studentEvents.get(id))
                .map(List::size)
                .orElse(0);
    }

    public void Append(Event event) {
        studentEvents.computeIfAbsent(event.getStreamId(), k -> new ArrayList<>())
                .add(event);

        students.put(
                event.getStreamId(),
                getStudent(event.getStreamId())
                        .orElse(null)
        );
    }

    public Optional<Student> getStudent(UUID studentId) {
        if (!studentEvents.containsKey(studentId)) {
            return Optional.empty();
        }

        var student = new Student();
        studentEvents.get(studentId)
                .forEach(student::Apply);

        return Optional.of(student);
    }

    public Optional<Student> getStudentView(UUID studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }
}
