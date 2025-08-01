package com.example.brightuicourse.ui.student;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {
    private StudentDao studentDao;
    private LiveData<List<Student>> allStudents;
    private final ExecutorService executorService;

    public StudentRepository(Application application) {
        StudentDatabase database = StudentDatabase.getDatabase(application);
        studentDao = database.studentDao();
        allStudents = studentDao.getAllStudents();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public void insert(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void update(Student student) {
        executorService.execute(() -> studentDao.update(student));
    }

    public void delete(Student student) {
        executorService.execute(() -> studentDao.delete(student));
    }
}