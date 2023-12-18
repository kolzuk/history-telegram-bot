package bot.infrastructure.repositories;

import bot.models.GameUser;
import org.telegram.telegrambots.meta.api.objects.games.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class UserRepository {
    private final Connection connection;
    public UserRepository(Connection connection) {
        this.connection = connection;
    }
    public GameUser getUser(int userId) throws SQLException {
        final String sqlQuery =
                """
                SELECT user_id, username, score
                FROM users
                WHERE user_id = ?
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, userId);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        int newUserId = resultSet.getInt(1);
        String username = resultSet.getString(2);
        int score = resultSet.getInt(3);

        return new GameUser(newUserId, username, score);
    }

    public void addUser(int userId, String username) throws SQLException {
        final String sqlQuery =
                """
                INSERT INTO users(user_id, username, score) 
                VALUES (?, ?, ?);
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        statement.setInt(1, userId);
        statement.setString(2, username);
        statement.setInt(3, 0);

        statement.execute();
    }

    public boolean hasUser(int userId) throws SQLException {
        final String sqlQuery =
                """
                SELECT COUNT(*)
                FROM users
                WHERE user_id = ?;
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        statement.setInt(1, userId);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        int count = resultSet.getInt(1);

        return count == 1;
    }

    public void incrementScore(int userId) throws SQLException {
        final String sqlQuery =
                """
                UPDATE users SET score = score + 1
                WHERE user_id = ?; 
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        statement.setInt(1, userId);
        statement.execute();
    }

    public List<GameUser> getTopKUsers(int k) throws SQLException {
        final String sqlQuery =
                """
                SELECT user_id, username, score
                FROM users
                ORDER BY users.score DESC;
                """;

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        ArrayList<GameUser> users = new ArrayList<>();

        while (k > 0 && resultSet.next()) {
            int userId = resultSet.getInt(1);
            String username = resultSet.getString(2);
            int score = resultSet.getInt(3);

            users.add(new GameUser(userId, username, score));
        }

        return users;
    }
}
