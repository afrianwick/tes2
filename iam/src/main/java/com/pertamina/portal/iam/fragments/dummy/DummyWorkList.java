package com.pertamina.portal.iam.fragments.dummy;

import com.pertamina.portal.iam.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyWorkList {

    public static final List<Task> ITEMS = new ArrayList<Task>();
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();
    private static final int COUNT = 25;
    public static final String[] ARR_CODE = {"LEV", "CLV", "MCL", "MPPK", "RMJ", "SKMJ",
        "PAN-PER", "PAN-COM", "PAN-ALM", "PAN-NPW", "PAN-MAR", "PAN-UNF", "PAN-EDU",
        "PAN-BAN", "PAN-BPJ", "PAN-DPL", "DPKP"};

    static {
        for (int i = 0; i < ARR_CODE.length; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Task createDummyItem(int position) {
        String code = ARR_CODE[position];
        String id = String.valueOf(position);
        boolean statusNotif = position < 4 ? true : false;
        String processName = position > 4 ? "Review Draft RMJ" : "Manager Approval";
        Task task = new Task(id, statusNotif, code + "-" + position + "0160314-00760010-1820",
                processName,
                "worklist_name by Trainee 8");
        return task;
    }
}
