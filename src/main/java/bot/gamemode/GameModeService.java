package bot.gamemode;

import bot.infrastructure.repositories.UserRepository;
import bot.models.GameUser;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class GameModeService {
    private final IGameMode imageGameMode;
    private final IGameMode textGameMode;
    private IGameMode currentGameMode;
    private UserRepository userRepository;

    public GameModeService(ImageGameMode imageGameMode,
                           TextGameMode textGameMode,
                           UserRepository userRepository) {
        this.imageGameMode = imageGameMode;
        this.textGameMode = textGameMode;
        this.userRepository = userRepository;
    }

    public IGameMode getCurrentGameMode() {
        return currentGameMode;
    }

    public String setCurrentGameMode(String gameMode) {
        return switch (gameMode) {
            case "1" -> {
                currentGameMode = textGameMode;
                yield "Выбран режим игры: угадать историческую личность по описанию.";
            }
            case "2" -> {
                currentGameMode = imageGameMode;
                yield "Выбран режим игры: угадать историческю личность по изображению.";
            }
            default -> {
                Logger.getAnonymousLogger().info("Неправильный режим игры");
                throw new IllegalArgumentException();
            }
        };
    }

    public void askQuestion(String chatId) {
        currentGameMode.askQuestion(chatId);
    }

    public void answerToQuestion(Update update, String answer) {
        if (currentGameMode.answerToQuestion(update.getMessage().getChatId().toString(), answer)) {
            try {
                userRepository.incrementScore(update.getMessage().getFrom().getId().intValue());
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
    }

    public GameUser getUser(int userId) {
        GameUser gameUser;
        try {
            gameUser = userRepository.getUser(userId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return gameUser;
    }

    public boolean hasUser(int userId) {
        boolean hasUser = false;

        try {
            hasUser = userRepository.hasUser(userId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return hasUser;
    }

    public void addUser(int userId, String userName) {
        try {
            userRepository.addUser(userId, userName);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<GameUser> getTopUsers(int k) {
        List<GameUser> users;

        try {
            users = userRepository.getTopKUsers(k);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return users;
    }
}
