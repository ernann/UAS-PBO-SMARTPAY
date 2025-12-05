package com.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderStorage {

    private static final String FILE_NAME = "reminders.dat";

    // simpan list Reminder ke file
    public static void save(List<Reminder> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load list Reminder dari file
    public static List<Reminder> load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Reminder>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
