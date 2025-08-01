package com.example.brightuicourse.ui.student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fullName;
    private String courseName;
    private String duration;
    private long enrolledDate; // Timestamp
    private String gender;     // ✅ NEW field

    public Student(String fullName, String courseName, String duration, long enrolledDate, String gender) {
        this.fullName = fullName;
        this.courseName = courseName;
        this.duration = duration;
        this.enrolledDate = enrolledDate;
        this.gender = gender;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public long getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(long enrolledDate) { this.enrolledDate = enrolledDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
