package com.app.Transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RiwayatPembayaran {

    private static final String FILE_NAME = "riwayat_penerima.dat";
    private static List<RiwayatItem> list = new ArrayList<>();

    static {
        load();
    }

    public static void tambah(RiwayatItem item) {
        list.add(item);
        save(); 
    }

    public static List<RiwayatItem> getList() {
        return list;
    }

  
    private static void save() {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Gagal simpan riwayat: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    private static void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            list = (List<RiwayatItem>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Gagal load riwayat: " + e.getMessage());
        }
    }
}
