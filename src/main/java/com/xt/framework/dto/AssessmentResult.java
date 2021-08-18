package com.xt.framework.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author tao.xiong
 * @Description 测评结果
 * @Date 2021/8/2 18:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssessmentResult {
    /**
     * 危险因素
     */
    private HazardsAnalysis hazardsAnalysis;

    /**
     * 身体评估结果（⼈形图）
     */
    private List<BodyAssessment> bodyAssessment;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class HazardsAnalysis {
        private String diagnose;
        //api 返回String
        private List<Hazard> hazards;

        //重写set方法转换
        public void setHazards(String hazards) {
            this.hazards = JSONArray.parseArray(hazards,Hazard.class);
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        private static class Hazard {
            private String icon;
            private String keyword;
            private String valueText;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class BodyAssessment {
        private String key;
        private List<Item> items;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        private static class Item {
            private String text;
            private String color;
        }
    }
}
