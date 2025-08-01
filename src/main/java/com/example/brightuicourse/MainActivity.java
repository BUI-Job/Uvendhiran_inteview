package com.example.brightuicourse;

import static com.example.brightuicourse.ui.student.AddEditStudentActivity.EXTRA_GENDER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.brightuicourse.ui.student.AddEditStudentActivity;
import com.example.brightuicourse.ui.student.Student;
import com.example.brightuicourse.ui.student.StudentViewModel;
import com.example.brightuicourse.ui.student.adapter.StudentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_STUDENT_REQUEST = 1;
    public static final int EDIT_STUDENT_REQUEST = 2;

    private StudentViewModel studentViewModel;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        FloatingActionButton fabAddStudent = findViewById(R.id.fab_add_student);
        fabAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditStudentActivity.class);
            startActivityForResult(intent, ADD_STUDENT_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        adapter = new StudentAdapter();
        recyclerView.setAdapter(adapter);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(this, students -> adapter.setStudents(students));

        adapter.setOnItemClickListener(student -> {
            Intent intent = new Intent(MainActivity.this, AddEditStudentActivity.class);
            intent.putExtra(AddEditStudentActivity.EXTRA_ID, student.getId());
            intent.putExtra(AddEditStudentActivity.EXTRA_FULL_NAME, student.getFullName());
            intent.putExtra(AddEditStudentActivity.EXTRA_COURSE_NAME, student.getCourseName());
            intent.putExtra(AddEditStudentActivity.EXTRA_DURATION, student.getDuration());
            intent.putExtra(AddEditStudentActivity.EXTRA_ENROLLED_DATE, student.getEnrolledDate());
            intent.putExtra(AddEditStudentActivity.EXTRA_GENDER, student.getGender());
            startActivityForResult(intent, EDIT_STUDENT_REQUEST);
        });

        adapter.setOnItemLongClickListener(student -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete " + student.getFullName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        studentViewModel.delete(student);
                        Toast.makeText(this, "Student deleted!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditStudentActivity.EXTRA_ID, -1);
            String fullName = data.getStringExtra(AddEditStudentActivity.EXTRA_FULL_NAME);
            String courseName = data.getStringExtra(AddEditStudentActivity.EXTRA_COURSE_NAME);
            String duration = data.getStringExtra(AddEditStudentActivity.EXTRA_DURATION);
            String gender = data.getStringExtra(AddEditStudentActivity.EXTRA_GENDER); // or however you capture gender

            long enrolledDate = data.getLongExtra(AddEditStudentActivity.EXTRA_ENROLLED_DATE, 0);

            if (fullName == null || courseName == null || duration == null || enrolledDate == 0) {
                Toast.makeText(this, "Student not saved/updated.", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student(fullName, courseName, duration, enrolledDate, gender);

            if (requestCode == ADD_STUDENT_REQUEST) {
                studentViewModel.insert(student);
                Toast.makeText(this, "Student saved!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == EDIT_STUDENT_REQUEST && id != -1) {
                student.setId(id);
                studentViewModel.update(student);
                Toast.makeText(this, "Student updated!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}