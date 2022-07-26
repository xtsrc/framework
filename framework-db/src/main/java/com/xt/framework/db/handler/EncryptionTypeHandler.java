package com.xt.framework.db.handler;

import com.xt.framwork.common.core.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author tao.xiong
 * @date 2022/4/30 13:15
 */
@MappedTypes({String.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
@Component
public class EncryptionTypeHandler extends BaseTypeHandler<Object> {
    static List<String> fields = Arrays.asList("phone", "idCard", "imAccid", "imToken","token");

    @Value("${framework.secret.salt}")
    private String salt;

    private boolean isNotEncryptionColumn(String name) {
        return !fields.contains(name);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isBlank((String) o)) {
            return;
        }
        preparedStatement.setString(i, EncryptUtil.doFinal(EncryptUtil.ENCRYPT_MODE, EncryptUtil.AES, (String) o, salt));
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
        return EncryptUtil.doFinal(EncryptUtil.DECRYPT_MODE, EncryptUtil.AES, columnValue, salt);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(EncryptUtil.DECRYPT_MODE, EncryptUtil.AES, columnValue, salt);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(EncryptUtil.DECRYPT_MODE, EncryptUtil.AES, columnValue, salt);
    }
}
