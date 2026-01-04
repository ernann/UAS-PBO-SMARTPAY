package com.app.Transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatPembayaran {

    private static Map<String, List<RiwayatItem>> data = new HashMap<>();

    private static final String FILE_NAME = "riwayat_transfer.ser";

    public static void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            data = new HashMap<>();
            System.out.println("Riwayat kosong (file belum ada)");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            data = (Map<String, List<RiwayatItem>>) in.readObject();
            System.out.println("✓ Riwayat transfer berhasil dimuat");
        } catch (Exception e) {
            e.printStackTrace();
            data = new HashMap<>();
        }
    }

    private static void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(data);
            System.out.println("✓ Riwayat transfer disimpan permanen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RiwayatItem> getListByUser(String smartId) {
        return data.computeIfAbsent(smartId, k -> new ArrayList<>());
    }

    public static void tambah(String smartId, RiwayatItem item) {
        getListByUser(smartId).add(0, item); 
        save();
    }
}
