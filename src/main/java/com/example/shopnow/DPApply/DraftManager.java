package com.example.shopnow.DPApply;

import java.util.HashMap;
import java.util.Map;

public class DraftManager {
    private static DraftManager instance;
    private final Map<String, com.example.shopnow.DPApply.CartMemento> drafts = new HashMap<>();

    private DraftManager() {}

    public static synchronized DraftManager getInstance() {
        if (instance == null) {
            instance = new DraftManager();
        }
        return instance;
    }

    public void saveDraft(String customerPhone, com.example.shopnow.DPApply.CartMemento memento) {
        drafts.put(customerPhone.trim(), memento);
    }

    public boolean hasDraft(String customerPhone) {
        return drafts.containsKey(customerPhone.trim());
    }

    public com.example.shopnow.DPApply.CartMemento getDraft(String customerPhone) {
        return drafts.get(customerPhone.trim());
    }

    public void clearDraft(String customerPhone) {
        drafts.remove(customerPhone.trim());
    }
}
