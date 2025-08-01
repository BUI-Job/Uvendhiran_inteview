package com.example.brightuicourse.ui.student;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students ORDER BY fullName ASC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM students WHERE id = :id")
    LiveData<Student> getStudentById(int id);
}