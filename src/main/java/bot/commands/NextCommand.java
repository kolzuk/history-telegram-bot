package bot.commands;

import bot.HistoricalBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NextCommand implements ICommand {
    private final HistoricalBot bot;
    public NextCommand(HistoricalBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(Update update) {
        bot.getGameMode().askQuestion(update.getMessage().getChatId().toString());
    }
}
