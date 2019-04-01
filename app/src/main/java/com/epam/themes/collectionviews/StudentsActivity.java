package com.epam.themes.collectionviews;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import com.epam.cleancodetest.R;
import com.epam.themes.backend.IWebService;
import com.epam.themes.backend.StudentsWebService;
import com.epam.themes.backend.entities.Student;
import com.epam.themes.collectionviews.recyclerview.StudentTouchCallback;
import com.epam.themes.collectionviews.recyclerview.StudentsAdapter;
import com.epam.themes.util.ICallback;
import com.epam.themes.util.StudentAdapterCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.epam.themes.backend.StudentsWebService.NULL_ID;
import static com.epam.themes.backend.StudentsWebService.students;
import static com.epam.themes.backend.StudentsWebService.MAX_COMPLETED_HOMEWORK_COUNT;


public class StudentsActivity extends AppCompatActivity {

    public static final int PAGE_SIZE = 10;
    public static final int MAX_VISIBLE_ITEMS = 40;
    public static final String IS_SHOW_LAST_VIEW_AS_LOADING_STATE_KEY = "isShowLastViewAsLoadingStateKey";
    public static final String IS_LOAD_COMPLETED_STATE_KEY = "isLoadCompleteStateKey";
    public static final String LINEAR_LAYOUT_MANAGER_STATE_KEY = "linearLayoutManagerStateKey";
    public static final String STUDENT_LIST_STATE_KEY = "studentListStateKey";
    private final IWebService<Student> studentsWebService = new StudentsWebService();

    private boolean isLoadCompleted = false;
    private boolean isLoading = false;
    private StudentsAdapter studentsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton addStudentImageButton;
    private Random random = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_students);

        setupAddButton();
        setupRecyclerView();

        if (savedInstanceState != null) {
            studentsAdapter.addItems(savedInstanceState.<Student>getParcelableArrayList(STUDENT_LIST_STATE_KEY));
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY));
            studentsAdapter.setShowLastViewAsLoading(savedInstanceState.getBoolean(IS_SHOW_LAST_VIEW_AS_LOADING_STATE_KEY));
            isLoadCompleted = savedInstanceState.getBoolean(IS_LOAD_COMPLETED_STATE_KEY);
        } else {
            loadMoreItems(0, PAGE_SIZE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY, linearLayoutManager.onSaveInstanceState());
        outState.putParcelableArrayList(STUDENT_LIST_STATE_KEY, (ArrayList<? extends Parcelable>) studentsAdapter.getItems());
        outState.putBoolean(IS_SHOW_LAST_VIEW_AS_LOADING_STATE_KEY, studentsAdapter.getIsShowLastViewAsLoading());
        outState.putBoolean(IS_LOAD_COMPLETED_STATE_KEY, isLoadCompleted);
    }

    private void setupAddButton() {
        addStudentImageButton = findViewById(R.id.addStudentImageButton);
        addStudentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Student newStudent = new Student()
                        .setId(NULL_ID)
                        .setName(students.get(random.nextInt(students.size())))
                        .setHomeworkCount(random.nextInt(MAX_COMPLETED_HOMEWORK_COUNT));
                int insertPosition = linearLayoutManager.findFirstVisibleItemPosition() + 1;

                studentsWebService.addEntity(insertPosition, newStudent, new ICallback<Long>() {
                    @Override
                    public void onResult(Long result) {
                        newStudent.setId(result);
                    }
                });
                studentsAdapter.insertItem(insertPosition, newStudent);
            }
        });
    }

    private void setupRecyclerView() {
        final RecyclerView studentsRecyclerView = findViewById(android.R.id.list);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        studentsRecyclerView.setLayoutManager(linearLayoutManager);

        studentsAdapter = new StudentsAdapter(this, new StudentAdapterCallback() {

            @Override
            public void onStudentChange(Student student) {
                studentsWebService.setHomeworkCount(student.getId(), student.getHomeworkCount());
            }

            @Override
            public void onStudentRemove(long id) {
                studentsWebService.removeEntity(id);
                checkNeedAndLoadMore();
            }
        });

        studentsRecyclerView.setAdapter(studentsAdapter);
        studentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                checkNeedAndLoadMore();
            }
        });

        new ItemTouchHelper(new StudentTouchCallback(studentsAdapter)).attachToRecyclerView(studentsRecyclerView);
    }

    private void checkNeedAndLoadMore() {
        int totalItemCount = linearLayoutManager.getItemCount() - 1;

        if (totalItemCount >= MAX_VISIBLE_ITEMS || isLoadCompleted) {
            studentsAdapter.setShowLastViewAsLoading(false);
            return;
        }

        int visibleItemCount = linearLayoutManager.getChildCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems(totalItemCount, totalItemCount + PAGE_SIZE);
            }
        }
    }

    private void loadMoreItems(final int startPosition, final int endPosition) {
        isLoading = true;
        studentsAdapter.setShowLastViewAsLoading(true);
        studentsWebService.getEntities(startPosition, endPosition, new ICallback<List<Student>>() {

            @Override
            public void onResult(List<Student> resultList) {
                if (resultList != null) {
                    if (resultList.size() < PAGE_SIZE) {
                        isLoadCompleted = true;
                    }

                    studentsAdapter.addItems(resultList);
                } else {
                    isLoadCompleted = true;
                }

                isLoading = false;
            }
        });
    }
}