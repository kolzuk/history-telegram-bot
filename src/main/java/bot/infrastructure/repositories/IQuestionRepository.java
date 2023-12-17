package bot.infrastructure.repositories;

import bot.models.TextQuestion;

import java.sql.SQLException;

public interface IQuestionRepository {
    TextQuestion getRandomQuestion() throws SQLException;
}
