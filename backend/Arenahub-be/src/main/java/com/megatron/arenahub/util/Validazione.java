package com.megatron.arenahub.util;

public class Validazione {

    public static boolean mancante(String testo) {
        return testo == null || testo.isBlank();
    }
}
