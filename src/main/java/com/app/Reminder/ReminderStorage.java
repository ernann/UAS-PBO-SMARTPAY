package com.app.Reminder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderStorage {
    private static final String FILE_NAME = "reminders.dat";

    public static void save(Map<String, List<Reminder>> userReminders) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(userReminders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<Reminder>> load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<String, List<Reminder>>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static List<Reminder> loadForUser(String smartId) {
        Map<String, List<Reminder>> allReminders = load();

        if (!allReminders.containsKey(smartId)) {
            allReminders.put(smartId, new ArrayList<>());
            save(allReminders);
        }

        return allReminders.get(smartId);
    }

    public static void saveForUser(String smartId, List<Reminder> reminders) {
        Map<String, List<Reminder>> allReminders = load();
        allReminders.put(smartId, reminders);
        save(allReminders);
    }
}
