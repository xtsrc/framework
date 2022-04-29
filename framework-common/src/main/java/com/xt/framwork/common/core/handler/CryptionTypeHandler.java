package com.xt.framwork.common.core.handler;

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
public class CryptionTypeHandler extends BaseTypeHandler<Object> {
    static List<String> fields = Arrays.asList("phone", "IdCard", "imAccid", "imToken",
            "recordPhone", "recordIdCard", "doctorImAccid", "userImAccid");

    @Value("${xt.secret.db.web}")
    private String salt;

    private boolean isNotCryption(String name) {
        return !fields.contains(name);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (StringUtils.isBlank((String) o)) {
            return;
        }
        preparedStatement.setString(i, EncryptUtil.doFinal(1, EncryptUtil.RSA, (String) o, salt));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        String columnValue = resultSet.getString(s);
        if (isNotCryption(s)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.RSA, columnValue, salt);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.RSA, columnValue, salt);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        if (StringUtils.isBlank(columnValue)) {
            return columnValue;
        }
        return EncryptUtil.doFinal(2, EncryptUtil.RSA, columnValue, salt);
    }
}
