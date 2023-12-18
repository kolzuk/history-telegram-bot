package bot;

import bot.commands.CommandContainer;
import bot.commands.ICommand;
import bot.gamemode.GameModeService;
import bot.gamemode.IGameMode;
import bot.gamemode.ImageGameMode;
import bot.gamemode.TextGameMode;
import bot.infrastructure.repositories.ImageQuestionRepository;
import bot.infrastructure.repositories.TextQuestionRepository;
import bot.infrastructure.repositories.UserRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.games.Game;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class HistoricalBot extends TelegramLongPollingBot {
    private static final String COMMAND_PREFIX = "/";
    private static final String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");
    private static final String BOT_NAME = "HistoryKolzukBot";
    private final GameModeService GAME_MODE_SERVICE;
    private final CommandContainer commandContainer = new CommandContainer(this);

    public HistoricalBot() {
        super(BOT_TOKEN);

        String url = System.getenv("DB_URL");
        String login = System.getenv("DB_LOGIN");
        String password = System.getenv("DB_PASSWORD");

        Connection connection;
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Can`t connection to database");
        }

        TextQuestionRepository textQuestionRepository = new TextQuestionRepository(connection);
        UserRepository userRepository = new UserRepository(connection);
        ImageQuestionRepository imageQuestionRepository = new ImageQuestionRepository();

        TextGameMode textGameMode = new TextGameMode(this, textQuestionRepository);
        ImageGameMode imageGameMode = new ImageGameMode(this, imageQuestionRepository);

        GAME_MODE_SERVICE = new GameModeService(imageGameMode, textGameMode, userRepository);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            if (messageText.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = messageText.split(" ")[0].toLowerCase().trim();

                ICommand command = commandContainer.getCommand(commandIdentifier);
                command.execute(update);
            } else {
                GAME_MODE_SERVICE.answerToQuestion(update, messageText);
            }
        } else if (update.hasCallbackQuery()) {
            String gameMode = update.getCallbackQuery().getData();

            EditMessageText newMessage = new EditMessageText();

            newMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
            newMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            newMessage.setText(GAME_MODE_SERVICE.setCurrentGameMode(gameMode));

            try {
                execute(newMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public GameModeService getGameModeService() {
        return GAME_MODE_SERVICE;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
