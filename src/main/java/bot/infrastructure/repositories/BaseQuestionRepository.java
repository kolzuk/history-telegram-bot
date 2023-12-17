package bot.infrastructure.repositories;

import java.sql.*;
import java.util.Random;

public abstract class BaseQuestionRepository implements IQuestionRepository {
    protected Connection connection;
    protected Random randomGenerator;
    protected BaseQuestionRepository(Connection connection) {
        this.connection = connection;
        this.randomGenerator = new Random();
    }

    int getTableSize(String tableName) throws SQLException {
        final String sqlQuery =
                """
                SELECT COUNT(*) FROM text_questions
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        return resultSet.getInt(1);
    }

    public int getRandomQuestionId(int upperBound) {
        return randomGenerator.nextInt(upperBound) + 1;
    }
}
