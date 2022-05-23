package com.xt.framwork.common.core.handler;

import com.xt.framwork.common.core.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author tao.xiong
 * @Date 2022/4/30 13:15
 */
@MappedTypes({String.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
@Component
public class EncryptionHandler extends BaseTypeHandler<Object> {
    static List<String> fields = Arrays.asList("phone", "IdCard", "imAccid", "imToken",
            "recordPhone", "recordIdCard", "doctorImAccid", "userImAccid");
    //通用写死，定制化consul取值项目单独引入
    protected static final String salt="xt_test";

    private boolean isNotEncryptionColumn(String name) {
        return !fields.contains(name);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isBlank((String) o)) {
            return;
        }
        preparedStatement.setString(i, EncryptUtil.doFinal(1, EncryptUtil.AES, (String) o, salt));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        String columnValue = resultSet.getString(s);
        if (isNotEncryptionColumn(s)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.AES, columnValue, salt);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.AES, columnValue, salt);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.AES, columnValue, salt);
    }
}
