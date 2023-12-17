package bot;

import bot.gamemode.IGameMode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class GameSession {
    private final Long sessionId;
    private final TelegramLongPollingBot bot;
    private IGameMode game;

    public GameSession(Long sessionId, TelegramLongPollingBot bot) {
        this.sessionId = sessionId;
        this.bot = bot;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public boolean isModeSelected() {
        return game != null;
    }

    public void setGameMode(IGameMode game) {
        this.game = game;
    }
}
