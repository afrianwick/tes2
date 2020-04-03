package com.pertamina.portal.iam.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.iam.R;

public class ButtonApprovalBuilder {

    private static final String TAG = "ButtonApprovalBuilder";
    private final View btApprove;
    private final View btReject;
    private final View btRetry;
    private final View btAskRevise;
    private final View btResubmit;
    private final View btContinue;
    private final View btCancel;
    private final View btSubmit;
    private final View btComplete;
    private final Activity activity;
    private String[] modes;
    private ApprovalModeListener listener;

    public ButtonApprovalBuilder(Activity activity) {
        this.activity = activity;
        btApprove = activity.findViewById(R.id.btApprove);
        btReject = activity.findViewById(R.id.btReject);
        btRetry = activity.findViewById(R.id.btRetry);
        btAskRevise = activity.findViewById(R.id.btAskRevise);
        btResubmit = activity.findViewById(R.id.btResubmit);
        btCancel = activity.findViewById(R.id.btCancel);
        btContinue = activity.findViewById(R.id.btContinue);
        btSubmit = activity.findViewById(R.id.btSubmit);
        btComplete = activity.findViewById(R.id.btComplete);

        if (btApprove != null)
            btApprove.setVisibility(View.GONE);

        if (btReject != null)
            btReject.setVisibility(View.GONE);

        if (btRetry != null)
            btRetry.setVisibility(View.GONE);

        if (btAskRevise != null)
            btAskRevise.setVisibility(View.GONE);

        if (btResubmit != null)
            btResubmit.setVisibility(View.GONE);

        if (btCancel != null)
            btCancel.setVisibility(View.GONE);

        if (btContinue != null)
            btContinue.setVisibility(View.GONE);

        if (btSubmit != null)
            btSubmit.setVisibility(View.GONE);

        if (btComplete != null)
            btComplete.setVisibility(View.GONE);
    }

    public ButtonApprovalBuilder setModes(String[] approvalModes) {
        this.modes = approvalModes;
        return this;
    }

    public ButtonApprovalBuilder setModeListener(ApprovalModeListener listener) {
        this.listener = listener;
        return this;
    }

    public ButtonApprovalBuilder build() {
        for (String mode : modes) {
            Log.d(TAG, "mode:" + mode);

            if (mode.equalsIgnoreCase(Constants.APROVAL_APPROVE)) {
                if (btApprove != null) {
                    Log.d(TAG, "approve");
                    btApprove.setVisibility(View.VISIBLE);
                    btApprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.approve();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_REJECT)) {
                if (btReject != null) {
                    Log.d(TAG, "reject");
                    btReject.setVisibility(View.VISIBLE);
                    btReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.reject();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_RETRY)) {
                if (btRetry != null) {
                    Log.d(TAG, "retry");
                    btRetry.setVisibility(View.VISIBLE);
                    btRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.retry();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_ASK_REVISE)) {
                if (btAskRevise != null) {
                    Log.d(TAG, "ask revise");
                    btAskRevise.setVisibility(View.VISIBLE);
                    btAskRevise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.askRevise();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_CONTINUE)) {
                if (btContinue != null) {
                    Log.d(TAG, "Continue");
                    btContinue.setVisibility(View.VISIBLE);
                    btContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.continueAction();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_RESUBMIT)) {
                if (btResubmit != null) {
                    Log.d(TAG, "Resubmit");
                    btResubmit.setVisibility(View.VISIBLE);
                    btResubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.resubmit();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_CANCEL)) {
                if (btCancel != null) {
                    Log.d(TAG, "Cancel");
                    btCancel.setVisibility(View.VISIBLE);
                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.cancel();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_SUBMIT)) {
                if (btSubmit != null) {
                    Log.d(TAG, "Submit");
                    btSubmit.setVisibility(View.VISIBLE);
                    btSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.submit();
                        }
                    });
                }
            } else if (mode.equalsIgnoreCase(Constants.APROVAL_COMPLETE)) {
                if (btComplete != null) {
                    Log.d(TAG, "Complete");
                    btComplete.setVisibility(View.VISIBLE);
                    btComplete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.complete();
                        }
                    });
                }
            }
        }

        return this;
    };

    public interface ApprovalModeListener {
        void approve();
        void reject();
        void retry();
        void askRevise();
        void continueAction();
        void cancel();
        void resubmit();
        void submit();
        void complete();
    }
}
