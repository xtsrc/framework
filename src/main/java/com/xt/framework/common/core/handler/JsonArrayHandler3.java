package com.xt.framework.common.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tao.xiong
 * @Description list转String 存储
 * @Date 2021/8/7 14:25
 */
@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonArrayHandler3<T> extends BaseTypeHandler<List<T>> {
    Class<T> innerType;

    public JsonArrayHandler3(Class<T> innerType) {
        this.innerType = innerType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(list));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return stringToList(resultSet.getString(s));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return stringToList(resultSet.getString(i));
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return stringToList(callableStatement.getString(i));
    }

    protected List<T> stringToList(String s) {
        return Strings.isNullOrEmpty(s) ? Lists.newArrayList() : JSONArray.parseArray(s).toJavaList(innerType);
    }

    public static class ListStringHandler extends JsonArrayHandler3<String> {
        public ListStringHandler() {
            super(String.class);
        }
    }
}
