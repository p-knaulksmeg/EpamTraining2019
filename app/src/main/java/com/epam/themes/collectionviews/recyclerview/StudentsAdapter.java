package com.epam.themes.collectionviews.recyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.epam.cleancodetest.R;
import com.epam.themes.backend.entities.Student;
import com.epam.themes.uicomponents.base.BaseViewHolder;
import com.epam.themes.util.StudentAdapterCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.themes.backend.StudentsWebService.NULL_ID;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Student> studentList = new ArrayList<>();
    private final StudentAdapterCallback studentAdapterCallback;
    private final LayoutInflater layoutInflater;
    private boolean isShowLastViewAsLoading = false;

    public StudentsAdapter(final Context context, StudentAdapterCallback studentAdapterCallback) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.studentAdapterCallback = studentAdapterCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup,
                                                      @ViewType final int viewType) {
        if (viewType == ViewType.STUDENT) {
            return new BaseViewHolder<>(new StudentView(viewGroup.getContext()));
        } else {
            return new BaseViewHolder<>(layoutInflater.inflate(R.layout.layout_progress, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == ViewType.STUDENT) {
            final Student student = studentList.get(position);

            ((StudentView) viewHolder.itemView)
                    .setStudentName(student.getName())
                    .setStudentHomeworkCount(student.getHomeworkCount());
        }
    }

    @ViewType
    @Override
    public int getItemViewType(final int position) {

        if (position < studentList.size()) {
            return ViewType.STUDENT;
        } else {
            return ViewType.LOADING;
        }
    }


    @Override
    public int getItemCount() {
        if (isShowLastViewAsLoading) {
            return studentList.size() + 1;
        } else {
            return studentList.size();
        }
    }

    public void setShowLastViewAsLoading(final boolean isShow) {
        if (isShow != isShowLastViewAsLoading) {
            isShowLastViewAsLoading = isShow;
            notifyDataSetChanged();
        }
    }

    public boolean getIsShowLastViewAsLoading() {
        return isShowLastViewAsLoading;
    }

    public List<Student> getItems() {
        return studentList;
    }

    public void addItems(final List<Student> studentList) {
        this.studentList.addAll(studentList);
        notifyItemRangeInserted(this.studentList.size() - studentList.size(), studentList.size());
    }


    public void insertItem(final int position, final Student student) {
        studentList.add(position, student);
        notifyItemInserted(position);
    }

    public void onItemDismiss(int adapterPosition) {

        Long id = studentList.get(adapterPosition).getId();

        if (getItemViewType(adapterPosition) == ViewType.STUDENT && id != NULL_ID) {
            studentList.remove(adapterPosition);
            studentAdapterCallback.onStudentRemove(id);
            notifyItemRemoved(adapterPosition);
        } else {
            notifyItemChanged(adapterPosition);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {

        if (getItemViewType(fromPosition) == ViewType.STUDENT && getItemViewType(toPosition) == ViewType.STUDENT) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(studentList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(studentList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @IntDef({ViewType.STUDENT, ViewType.LOADING})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {

        int STUDENT = 0;
        int LOADING = 1;
    }
}