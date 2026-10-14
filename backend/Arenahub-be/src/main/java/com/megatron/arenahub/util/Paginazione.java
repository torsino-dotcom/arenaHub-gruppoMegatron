package com.megatron.arenahub.util;

import com.megatron.arenahub.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class Paginazione {

    // page parte da 1 (default 1), limit default 20
    public static <T> List<T> applica(List<T> lista, Integer page, Integer limit) {
        int p = (page == null) ? 1 : page;
        int l = (limit == null) ? 20 : limit;
        if (p < 1 || l < 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "page e limit devono essere maggiori di 0");
        }
        int da = (int) Math.min((long) (p - 1) * l, lista.size());
        int a = (int) Math.min((long) da + l, lista.size());
        return new ArrayList<>(lista.subList(da, a));
    }
}
