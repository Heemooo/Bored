package com.bored.db;

import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUtil {

    @SneakyThrows
    public static <T> List<T> toObject(ResultSet resultSet, Class<T> tClass) {
        List<T> result = new ArrayList<>();
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            Field[] fields = tClass.getDeclaredFields();
            while (resultSet.next()) {
                T bean = tClass.getDeclaredConstructor().newInstance();
                for (int iterator = 0, length = metaData.getColumnCount(); iterator < length; iterator++) {
                    String columnName = metaData.getColumnName(iterator + 1);
                    Object columnValue = resultSet.getObject(iterator + 1);
                    if (columnValue instanceof Clob) {
                        Clob clob = (Clob) columnValue;
                        columnValue = clob.getSubString(1, (int) clob.length());
                    }
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            if (column.value().equalsIgnoreCase(columnName) && columnValue != null) {
                                ReflectUtil.setFieldValue(bean, field, columnValue);
                                break;
                            }
                        }
                    }
                }
                result.add(bean);
            }
        }
        return result;
    }
}
