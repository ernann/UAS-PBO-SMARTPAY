package com.app.Transfer;

import java.util.ArrayList;
import java.util.List;

public class RiwayatStorage {

    private static final List<RiwayatPembayaran> list = new ArrayList<>();

    public static void save(RiwayatPembayaran r) {
        list.add(r);
    }

    public static List<RiwayatPembayaran> load() {
        return list;
    }
}
