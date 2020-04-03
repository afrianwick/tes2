package com.pertamina.portal.iam.interfaces;

public interface MyWorkListView {

    void onDeleteFilter(int position);
    void onRefilter();
    void filterChecked(int poisition, boolean isChecked);
}
