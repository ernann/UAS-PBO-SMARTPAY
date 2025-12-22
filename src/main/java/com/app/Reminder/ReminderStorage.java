// [file name]: ReminderStorage.java
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

import com.app.User;

public class ReminderStorage {
    private static final String FILE_NAME = "reminders.dat";

    // Save reminders for all users
    public static void save(Map<String, List<Reminder>> userReminders) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(userReminders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load reminders for all users
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

    // Load reminders for specific user
    public static List<Reminder> loadForUser(String smartId) {
        Map<String, List<Reminder>> allReminders = load();
        return allReminders.getOrDefault(smartId, new ArrayList<>());
    }

    // Save reminder for specific user
    public static void saveForUser(String smartId, List<Reminder> reminders) {
        Map<String, List<Reminder>> allReminders = load();
        allReminders.put(smartId, reminders);
        save(allReminders);
    }

    // Add new reminder for user
    public static void addReminder(String smartId, Reminder reminder) {
        List<Reminder> userReminders = loadForUser(smartId);
        userReminders.add(reminder);
        saveForUser(smartId, userReminders);
    }

    // Remove reminder for user
    public static void removeReminder(String smartId, Reminder reminder) {
        List<Reminder> userReminders = loadForUser(smartId);
        userReminders.remove(reminder);
        saveForUser(smartId, userReminders);
    }
}