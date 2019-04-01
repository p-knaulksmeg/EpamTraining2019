package com.epam.themes.backend;

import android.os.Handler;
import android.os.Looper;

import com.epam.themes.backend.entities.Student;
import com.epam.themes.util.ICallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StudentsWebService implements IWebService<Student> {

    public static final int MAX_HOMEWORK_COUNT = 10;
    public static final long NULL_ID = -1L;
    
    public static List<String> nameStudents = Arrays.asList(
            "Aliaksei Shvants",
            "Maryia Senkevich",
            "Pavel Klimovich",
            "yahor shymanchyk",
            "Anton Liaskevich",
            "Yahor Berdnikau",
            "MAKSIM ZHANHIALIOU",
            "ULADZISLAU SITSKO",
            "Goncharov Alexander",
            "MAKSIM NASALEVICH",
            "Vitali Kullikouski",
            "ALIAKSANDR LITSKEVICH",
            "Kiryl Shreyter",
            "Konopeshko Aleksei",
            "NATALLIA BONDARAVA",
            "ALIAKSEI HALAVACH",
            "Maksim Siamashka",
            "Vladyslav Vsemirnov"
    );
    private Long lastId;
    private List<Student> studentList = new ArrayList<>();
    private Random random = new Random();
    private Handler handler = new Handler(Looper.getMainLooper());

    public StudentsWebService() {
        createStudentList();
    }

    private void createStudentList() {
        for (int i = 0; i < nameStudents.size(); i++) {
            lastId = (long) i;
            Student student = new Student();
            student.setId(lastId);
            student.setHwCount(1 + random.nextInt(5));
            student.setName(nameStudents.get(i));
            studentList.add(student);
        }
    }

    @Override
    public void getEntities(final ICallback<List<Student>> callback) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onResult(studentList);
            }
        }, 1000);
    }

    @Override
    public void getEntities(final int startRange,
                            final int endRange,
                            final ICallback<List<Student>> callback) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (startRange < studentList.size()) {
                    if (endRange > studentList.size()) {
                        callback.onResult(studentList.subList(startRange, studentList.size()));
                    } else {
                        callback.onResult(studentList.subList(startRange, endRange));
                    }
                } else {
                    callback.onResult(null);
                }
            }
        }, 1000);
    }

    @Override
    public void removeEntity(final Long id) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < studentList.size(); i++) {
                    if (studentList.get(i).getId().equals(id)) {
                        studentList.remove(i);

                        break;
                    }
                }
            }
        }, 1000);
    }

    public void addEntity(final int position, final Student student, final ICallback<Long> callback) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastId++;
                student.setId(lastId);
                studentList.add(position, student);
                callback.onResult(lastId);
            }
        }, 1000);
    }
    @Override
    public void setHomeworkCount(final Long id, final int homeworkCount) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < studentList.size(); i++) {
                    if (studentList.get(i).getId().equals(id)) {
                        studentList.get(i).setHwCount(homeworkCount);

                        break;
                    }
                }
            }
        }, 1000);
    }
}
