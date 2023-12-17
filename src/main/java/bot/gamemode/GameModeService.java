package bot.gamemode;

import java.util.logging.Logger;

public class GameModeService {
    private final IGameMode imageGameMode;
    private final IGameMode textGameMode;
    private IGameMode currentGameMode;

    public GameModeService(ImageGameMode imageGameMode,
                           TextGameMode textGameMode) {
        this.imageGameMode = imageGameMode;
        this.textGameMode = textGameMode;
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

    public void answerToQuestion(String chatId, String answer) {
        currentGameMode.answerToQuestion(chatId, answer);
    }
}
