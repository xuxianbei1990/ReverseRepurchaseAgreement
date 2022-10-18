package economy.reverse.repurchase.agreement.util.mysql;

import cn.hutool.core.date.LocalDateTimeUtil;
import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.model.GrahamIndex;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * @author: xuxianbei
 * Date: 2022/9/29
 * Time: 10:04
 * Version:V1.0
 */
public class MySqlBuffettIndex {

    public void executeCreateAndUpdate(List<GrahamIndex> fund110003s) {
        execute((connection, statementResultSet) -> {
            try {
                for (GrahamIndex fund110003 : fund110003s) {
                    executeUpdate(connection, statementResultSet, fund110003);
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

    private static void executeQuery(Connection connection, StatementResultSet statementResultSet, String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("SELECT unit, sum, create_date FROM fund%s WHERE create_date >= '2012-09-27'", code));
        ResultSet resultSet = preparedStatement.executeQuery();
        statementResultSet.setResultSet(resultSet);
        statementResultSet.setPreparedStatement(preparedStatement);
    }

    private static void executeUpdate(Connection connection, StatementResultSet statementResultSet, GrahamIndex fund110003) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO buffett_index (ratio, create_date) values ( ?, ?)");
        preparedStatement.setBigDecimal(1, fund110003.getRatio());
        preparedStatement.setDate(2, Date.valueOf(TimeThreadSafeUtils.dateTimeFormat(fund110003.getCreateDate(),
                TimeThreadSafeUtils.YYYY_MM_DD)));
        boolean resultSet = preparedStatement.execute();
        statementResultSet.setResultSet(preparedStatement.getResultSet());
        statementResultSet.setPreparedStatement(preparedStatement);
    }
}


