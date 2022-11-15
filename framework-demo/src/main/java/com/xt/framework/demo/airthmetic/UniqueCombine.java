package com.xt.framework.demo.airthmetic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author: tao.xiong
 * @Date: 2020/1/9 11:04
 * @Description:
 */
public class UniqueCombine {
    private static final String COMBINE_A = "A";
    private static final String COMBINE_B = "B";
    private static final String COMBINE_C = "C";
    private static final String COMBINE_D = "D";

    private static final int COMBINE_A_SIZE = 10;
    private static final int COMBINE_B_SIZE = 10;
    private static final int COMBINE_C_SIZE = 10;
    private static final int COMBINE_D_SIZE = 10;

    private static Map<String, Long> geneUniqueId(Long baseNum) {
        Map<String, Long> queryIdMap = Maps.newHashMap();
        long combineAid = baseNum % COMBINE_A_SIZE;
        long combineBid = (((baseNum / COMBINE_A_SIZE) % COMBINE_B_SIZE) + combineAid) % COMBINE_B_SIZE;
        long combineCid = (((baseNum / (COMBINE_A_SIZE * COMBINE_B_SIZE)) % COMBINE_C_SIZE) + combineAid)
                % COMBINE_C_SIZE;
        Long combineDid = ((baseNum / (COMBINE_A_SIZE * COMBINE_B_SIZE * COMBINE_C_SIZE)) + combineAid)
                % COMBINE_D_SIZE;
        queryIdMap.put(COMBINE_D, combineDid);
        queryIdMap.put(COMBINE_C, combineCid + COMBINE_D_SIZE);
        queryIdMap.put(COMBINE_B, combineBid + COMBINE_D_SIZE + COMBINE_C_SIZE);
        queryIdMap.put(COMBINE_A, combineAid + COMBINE_D_SIZE + COMBINE_C_SIZE + COMBINE_B_SIZE);
        return queryIdMap;
    }

    private static String genUniqueName(List<String> materials, Long baseNum) {
        if (CollectionUtils.isEmpty(materials)
                || materials.size() != COMBINE_A_SIZE + COMBINE_B_SIZE + COMBINE_C_SIZE + COMBINE_D_SIZE) {
            return null;
        }
        return geneUniqueId(baseNum).values().stream().map(value -> materials.get(value.intValue()))
                .collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        List<String> materials = Lists.newArrayList();
        IntStream.range(1, 41).forEach(i -> materials.add(String.valueOf(i)));
        System.out.println(genUniqueName(materials, 1L));
    }
}
