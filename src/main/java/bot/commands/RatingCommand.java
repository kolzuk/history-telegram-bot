package bot.commands;

import bot.HistoricalBot;
import bot.models.GameUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class RatingCommand implements ICommand {
    private final HistoricalBot bot;
    public RatingCommand(HistoricalBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(Update update) {
        List<GameUser> users = bot.getGameModeService().getTopUsers(10);

        StringBuilder messageTextBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); ++i) {
            GameUser gameUser = users.get(i);

            messageTextBuilder.append((i + 1) + ". ");

            if (i == 0) {
                messageTextBuilder.append("\uD83C\uDFC6 ");
            }

            messageTextBuilder.append(
                    gameUser.userName()
                    + ": "
                    + gameUser.score()
                    + "\n");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(messageTextBuilder.toString());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
