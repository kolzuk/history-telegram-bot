package bot.commands;

import bot.HistoricalBot;
import bot.infrastructure.repositories.UserRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StartCommand implements ICommand {
    private final HistoricalBot bot;
    public StartCommand(HistoricalBot bot) {
        this.bot = bot;
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Set buttons
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        firstButton.setText("1");
        firstButton.setCallbackData("1");

        InlineKeyboardButton secondButton = new InlineKeyboardButton();
        secondButton.setText("2");
        secondButton.setCallbackData("2");

        rowInline.add(firstButton);
        rowInline.add(secondButton);

        rowsInline.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();

        int userId = user.getId().intValue();
        String username = user.getUserName();

        if (!bot.getGameModeService().hasUser(userId)) {
            bot.getGameModeService().addUser(userId, username);
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("""
                Выберите режим игры:
                1) Текстовый режим
                2) Угадать историческую личность по изображению
                """);

        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup();

        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
