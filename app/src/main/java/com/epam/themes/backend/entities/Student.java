package com.epam.themes.backend.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

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
    private Long Id;
    private String Name;
    private int HomeworkCount;

    public Student() {
    }

    protected Student(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readLong();
        }
        Name = in.readString();
        HomeworkCount = in.readInt();
    }

    public Long getId() {
        return Id;
    }

    public Student setId(Long id) {
        this.Id = id;

        return this;
    }

    public String getName() {
        return Name;
    }

    public Student setName(String name) {
        this.Name = name;

        return this;
    }

    public int getHwCount() {
        return HomeworkCount;
    }

    public Student setHwCount(int hwCount) {
        HomeworkCount = hwCount;

        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        if (Id == null) {
            destination.writeByte((byte) 0);
        } else {
            destination.writeByte((byte) 1);
            destination.writeLong(Id);
        }
        destination.writeString(Name);
        destination.writeInt(HomeworkCount);
    }
}
