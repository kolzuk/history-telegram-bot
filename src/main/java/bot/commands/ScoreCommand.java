package bot.commands;

import bot.HistoricalBot;
import bot.models.GameUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class ScoreCommand implements ICommand {
    private final HistoricalBot bot;
    public ScoreCommand(HistoricalBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(Update update) {
        User user = update.getMessage().getFrom();

        Long userId = user.getId();

        GameUser gameUser;

        gameUser = bot.getGameModeService().getUser(userId.intValue());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Твои очки: " + gameUser.score());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
        }
    }
}
