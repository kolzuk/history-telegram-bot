package bot.infrastructure.repositories;

import bot.models.TextQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TextQuestionRepository extends BaseQuestionRepository {
    private final String TABLE_NAME = "text_questions";
    public TextQuestionRepository(Connection dataBaseConnection) {
        super(dataBaseConnection);
    }

    @Override
    public TextQuestion getRandomQuestion() throws SQLException {
        int tableSize = getTableSize(TABLE_NAME);
        int questionId = getRandomQuestionId(tableSize);

        final String sqlQuery =
                """
                SELECT question, answer
                FROM text_questions
                WHERE question_id = ?
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, questionId);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        String question = resultSet.getString(1);
        String answer = resultSet.getString(2);

        return new TextQuestion(question, answer);
    }
}
