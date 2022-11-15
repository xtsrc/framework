package com.xt.framework.demo.airthmetic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: tao.xiong
 * @Date: 2019/12/24 14:30
 * @Description:
 */
public class RecursionCombine {

    public static void combine(WorkSpace workSpace) {
        String currentKey = workSpace.getCurrentKey();
        String currentValue;
        while (true) {
            // 切换到下一个值
            if (workSpace.hasNextValue()) {
                workSpace.setCurrentValue(workSpace.getNextValue());
                currentValue = workSpace.getCurrentValue();
            } else {
                // 当前选项结束
                break;
            }
            // 取值
            workSpace.getOnceCombine().put(currentKey, currentValue);
            // 切换到下选项
            if (workSpace.hasNextKey()) {
                workSpace.setCurrentKey(workSpace.getNextKey());
                combine(workSpace);
                workSpace.setCurrentKey(currentKey);
                workSpace.setCurrentValue(currentValue);

            } else {
                // 所有选项结束
                workSpace.getCombine().add(Maps.newHashMap(workSpace.getOnceCombine()));
            }

        }
    }

    private static class WorkSpace {
        List<Map<String, String>> combine = Lists.newArrayList();
        Map<String, String> onceCombine = Maps.newHashMap();
        String currentKey;
        String currentValue;
        TreeMap<String, List<String>> select;

        WorkSpace(TreeMap<String, List<String>> select) {
            setSelect(select);
            setCurrentKey(select.firstKey());
        }

        List<Map<String, String>> getCombine() {
            return combine;
        }

        Map<String, String> getOnceCombine() {
            return onceCombine;
        }

        String getCurrentKey() {
            return currentKey;
        }

        void setCurrentKey(String currentKey) {
            this.currentKey = currentKey;
        }
        void setSelect(TreeMap<String, List<String>> select) {
            this.select = select;
        }

        String getCurrentValue() {
            return currentValue;
        }

        void setCurrentValue(String currentValue) {
            this.currentValue = currentValue;

        }

        String getNextKey() {
            return this.select.higherKey(currentKey);
        }

        String getNextValue() {
            List<String> values = select.get(currentKey);
            return values.get(values.indexOf(currentValue) + 1);
        }

        Boolean hasNextKey() {
            return StringUtils.isNotBlank(this.select.higherKey(currentKey));

        }

        Boolean hasNextValue() {
            List<String> values = select.get(currentKey);
            return values.indexOf(currentValue) + 1 < values.size();

        }
    }
}
