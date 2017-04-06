package ru.pascalman.translate.view;

import ru.pascalman.translate.presenter.LookupResponse;

public interface ListWithFindView extends ListView<LookupResponse>
{

    String getFindString();

}
