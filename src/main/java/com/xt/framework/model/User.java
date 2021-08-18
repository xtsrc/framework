package com.xt.framework.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.xt.framework.dto.AssessmentResult;
import com.xt.framework.common.core.handler.JsonArrayHandler;
import com.xt.framework.common.core.handler.ListToStringHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

/**
 * @author tao.xiong
 * @Description demo bean
 * @Date 2021/7/28 17:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User extends Model<User> {
    private static final long serialVersionUID = 5393185290185557134L;
    @TableField("userId")
    private Integer userId;
    /**
     * List<Object> 类型mysql转换
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = ListTypeHandlerB.class)
    private List<AssessmentResult.BodyAssessment> bodyAssessment;
    public static class ListTypeHandlerB extends JsonArrayHandler<AssessmentResult.BodyAssessment> {
        public ListTypeHandlerB() {
            super(AssessmentResult.BodyAssessment.class);
        }
    }

    /**
     * List<Integer> 类型mysql转换
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = ListToStringHandler.class)
    private List<Integer>  finishedDay;

    /**
     * Object 类型mysql 转换
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private AssessmentResult.HazardsAnalysis hazardsAnalysis;
}
