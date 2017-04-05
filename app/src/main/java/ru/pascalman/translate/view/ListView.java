package ru.pascalman.translate.view;

import java.util.List;

public interface ListView<T> extends View
{

    void showList(List<T> list);
    void showEmptyList();

}