package com.epam.themes.backend;

import com.epam.themes.util.ICallback;

import java.util.List;

public interface IWebService<T> {

    void getEntities(final ICallback<List<T>> callback);

    void getEntities(final int startRange,
                     final int endRange,
                     final ICallback<List<T>> callback);

    void removeEntity(final Long id);

    void addEntity(final int position,
                   final T student,
                   final ICallback<Long> callback);

    void setHomeworkCount(final Long id, final int homeworkCount);
}
