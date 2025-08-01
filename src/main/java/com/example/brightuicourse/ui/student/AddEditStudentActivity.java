package com.example.brightuicourse.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brightuicourse.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditStudentActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.studentrecordsapp.EXTRA_ID";
    public static final String EXTRA_FULL_NAME = "com.example.studentrecordsapp.EXTRA_FULL_NAME";
    public static final String EXTRA_COURSE_NAME = "com.example.studentrecordsapp.EXTRA_COURSE_NAME";
    public static final String EXTRA_DURATION = "com.example.studentrecordsapp.EXTRA_DURATION";
    public static final String EXTRA_ENROLLED_DATE = "com.example.studentrecordsapp.EXTRA_ENROLLED_DATE";
    public static final String EXTRA_GENDER = "com.example.studentrecordsapp.EXTRA_GENDER";

    private EditText editTextFullName;
    private Spinner spinnerCourseName, spinnerDuration, spinnerGender;
    private TextView textViewEnrolledDate;
    private Calendar calendar;
    private long enrolledDateTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        editTextFullName = findViewById(R.id.edit_text_full_name);
        spinnerCourseName = findViewById(R.id.spinner_course); // Changed
        spinnerDuration = findViewById(R.id.spinner_duration);
        spinnerGender = findViewById(R.id.spinner_gender);
        textViewEnrolledDate = findViewById(R.id.text_view_enrolled_date);
        Button buttonSave = findViewById(R.id.button_save);

        // Course Name Spinner
        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(
                this, R.array.course_options, android.R.layout.simple_spinner_item);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseName.setAdapter(courseAdapter);

        // Duration Spinner
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(
                this, R.array.duration_options, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(durationAdapter);

        // Gender Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        calendar = Calendar.getInstance();
        enrolledDateTimestamp = calendar.getTimeInMillis();

        textViewEnrolledDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                enrolledDateTimestamp = calendar.getTimeInMillis();
                updateDateLabel();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Student");

            editTextFullName.setText(intent.getStringExtra(EXTRA_FULL_NAME));

            enrolledDateTimestamp = intent.getLongExtra(EXTRA_ENROLLED_DATE, 0);
            calendar.setTimeInMillis(enrolledDateTimestamp);
            updateDateLabel();

            String course = intent.getStringExtra(EXTRA_COURSE_NAME);
            if (course != null) {
                int position = courseAdapter.getPosition(course);
                spinnerCourseName.setSelection(position);
            }

            String duration = intent.getStringExtra(EXTRA_DURATION);
            if (duration != null) {
                int position = durationAdapter.getPosition(duration);
                spinnerDuration.setSelection(position);
            }

            String gender = intent.getStringExtra(EXTRA_GENDER);
            if (gender != null) {
                int position = genderAdapter.getPosition(gender);
                spinnerGender.setSelection(position);
            }
        } else {
            setTitle("Add Student");
            updateDateLabel();
        }

        buttonSave.setOnClickListener(v -> saveStudent());
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        textViewEnrolledDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveStudent() {
        String fullName = editTextFullName.getText().toString().trim();
        String courseName = spinnerCourseName.getSelectedItem().toString(); // Changed
        String duration = spinnerDuration.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Please enter the full name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_FULL_NAME, fullName);
        data.putExtra(EXTRA_COURSE_NAME, courseName);
        data.putExtra(EXTRA_DURATION, duration);
        data.putExtra(EXTRA_ENROLLED_DATE, enrolledDateTimestamp);
        data.putExtra(EXTRA_GENDER, gender);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
