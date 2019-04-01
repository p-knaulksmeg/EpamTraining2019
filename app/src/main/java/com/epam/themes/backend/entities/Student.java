package com.epam.themes.backend.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private Long id;
    private String name;
    private int homeworkCount;

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student() {

    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {

        if (id == null) {
            destination.writeByte((byte) 0);
        } else {
            destination.writeByte((byte) 1);
            destination.writeLong(id);
        }

        destination.writeString(name);
        destination.writeInt(homeworkCount);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    protected Student(Parcel in) {

        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }

        name = in.readString();
        homeworkCount = in.readInt();
    }

    public Long getId() {

        return id;
    }

    public Student setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {

        return name;
    }

    public Student setName(String name) {
        this.name = name;

        return this;
    }

    public int getHomeworkCount() {

        return homeworkCount;
    }

    public Student setHomeworkCount(int homeworkCount) {
        this.homeworkCount = homeworkCount;

        return this;
    }


}
