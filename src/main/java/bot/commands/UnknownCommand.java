package bot.commands;

import bot.HistoricalBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements ICommand {
    private final HistoricalBot bot;
    public UnknownCommand(HistoricalBot bot) {
        this.bot = bot;
    }
    @Override
    public void execute(Update update) {
    }
}
