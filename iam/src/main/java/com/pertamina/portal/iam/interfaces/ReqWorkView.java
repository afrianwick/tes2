package com.pertamina.portal.iam.interfaces;

import com.pertamina.portal.iam.models.EmbassyModel;
import com.pertamina.portal.iam.models.PurposeModel;

public interface ReqWorkView {

    void onEmbassyClicked(EmbassyModel embassyModel);
    void onTypeClicked(int position);
    void onPurposeClicked(PurposeModel purposeModel, int position);
}
