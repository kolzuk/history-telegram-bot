package bot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;

public interface ICommand {
    void execute(Update update);
}
