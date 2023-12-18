package bot.commands;

import bot.HistoricalBot;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, ICommand> commands = new HashMap<>();
    private final ICommand unknownCommand;

    public CommandContainer(HistoricalBot bot) {
        commands.put("/start", new StartCommand(bot));
        commands.put("/next", new NextCommand(bot));
        commands.put("/score", new ScoreCommand(bot));
        commands.put("/rating", new RatingCommand(bot));

        unknownCommand = new UnknownCommand(bot);
    }

    public ICommand getCommand(String command) {
        return commands.getOrDefault(command, unknownCommand);
    }
}
