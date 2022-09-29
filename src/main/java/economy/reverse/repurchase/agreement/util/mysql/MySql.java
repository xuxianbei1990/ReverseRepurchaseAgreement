package economy.reverse.repurchase.agreement.util.mysql;

import cn.hutool.core.date.LocalDateTimeUtil;
import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author: xuxianbei
 * Date: 2022/9/29
 * Time: 10:04
 * Version:V1.0
 */
public class MySql {

    public void executeCreateAndUpdate(List<Fund110003> fund110003s, String code, String codeName) {
        execute((connection, statementResultSet) -> {
            try {
                executeCreateTable(connection, code, codeName);

                for (Fund110003 fund110003 : fund110003s) {
                    executeUpdate(connection, statementResultSet, fund110003, code);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


    public void execute(BiConsumer<Connection, StatementResultSet> consumer) {
        Properties properties = null;
        try {
            properties = Resources.getResourceAsProperties("application.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        StatementResultSet statementResultSet = new StatementResultSet();
        try {
            //创建连接----这里同时注册了驱动
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"),
                    properties.getProperty("password"));

            consumer.accept(connection, statementResultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statementResultSet.getPreparedStatement().close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Fund110003> executeSelect(String code) {
        List<Fund110003> list = new ArrayList<>();
        execute((connection, statementResultSet) -> {
            try {
                executeQuery(connection, statementResultSet, code);
                if (statementResultSet.getResultSet() != null) {
                    //结果集的遍历
                    while (statementResultSet.getResultSet().next()) {
                        Fund110003 fund110003 = new Fund110003();
                        fund110003.setUnit(statementResultSet.getResultSet().getBigDecimal(1));
                        fund110003.setSum(statementResultSet.getResultSet().getBigDecimal(2));
                        fund110003.setCreateDate(LocalDateTimeUtil.of(statementResultSet.getResultSet().getDate(3).toLocalDate()));
                        list.add(fund110003);
                    }
                    statementResultSet.getResultSet().close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return list;
    }

    private static void executeCreateTable(Connection connection, String code, String codeName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("CREATE TABLE if not exists `fund%s` (\n" +
                        "  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                        "  `unit` decimal(10,4) DEFAULT '0.00' COMMENT '单位净值',\n" +
                        "  `sum` decimal(10,4) DEFAULT '0.00' COMMENT '累计净值',\n" +
                        "  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                        "  PRIMARY KEY (`id`) USING BTREE\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=4517 DEFAULT CHARSET=utf8 COMMENT='%s';", code, codeName));
        preparedStatement.execute();
    }


    private static void executeQuery(Connection connection, StatementResultSet statementResultSet, String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("SELECT unit, sum, create_date FROM fund%s WHERE create_date >= '2012-09-27'", code));
        ResultSet resultSet = preparedStatement.executeQuery();
        statementResultSet.setResultSet(resultSet);
        statementResultSet.setPreparedStatement(preparedStatement);
    }

    private static void executeUpdate(Connection connection, StatementResultSet statementResultSet, Fund110003 fund110003, String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("INSERT INTO fund%s (unit, sum, create_date) values ( ?, ?, ?)", code));
        preparedStatement.setBigDecimal(1, fund110003.getUnit());
        preparedStatement.setBigDecimal(2, fund110003.getSum());
        preparedStatement.setDate(3, java.sql.Date.valueOf(TimeThreadSafeUtils.dateTimeFormat(fund110003.getCreateDate(),
                TimeThreadSafeUtils.YYYY_MM_DD)));
        boolean resultSet = preparedStatement.execute();
        statementResultSet.setResultSet(preparedStatement.getResultSet());
        statementResultSet.setPreparedStatement(preparedStatement);
    }
}


