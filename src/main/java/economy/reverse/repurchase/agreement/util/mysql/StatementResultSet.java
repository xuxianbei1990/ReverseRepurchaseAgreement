package economy.reverse.repurchase.agreement.util.mysql;

import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author: xuxianbei
 * Date: 2022/9/29
 * Time: 10:08
 * Version:V1.0
 */
@Data
public class StatementResultSet {
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
}
