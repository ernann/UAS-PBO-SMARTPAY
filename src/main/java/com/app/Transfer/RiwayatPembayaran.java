package com.app.Transfer;

import java.util.ArrayList;
import java.util.List;

public class RiwayatPembayaran {

    private static List<RiwayatItem> list = new ArrayList<>();

    public static void tambah(RiwayatItem item) {
        list.add(item);
    }

    public static List<RiwayatItem> getList() {
        return list;
    }
}
