package bot.gamemode;

import bot.infrastructure.repositories.TextQuestionRepository;
import bot.models.TextQuestion;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TextGameMode implements IGameMode {
    private final Logger LOGGER = Logger.getAnonymousLogger();
    private final TelegramLongPollingBot BOT;
    private final TextQuestionRepository TEXT_QUERY_REPOSITORY;
    private final Map<String, String> QUESTIONS = new HashMap<>();

    public TextGameMode(TelegramLongPollingBot bot,
                        TextQuestionRepository textQuestionRepository) {
        this.BOT = bot;
        this.TEXT_QUERY_REPOSITORY = textQuestionRepository;
    }

    @Override
    public void askQuestion(String chatId) {
        TextQuestion textQuestion;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        try {
            textQuestion = TEXT_QUERY_REPOSITORY.getRandomQuestion();
        } catch (SQLException exception) {
            LOGGER.info(exception.getMessage());
            return;
        }

        String question = textQuestion.question();

        sendMessage.setText(question);

        try {
            BOT.execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.info(e.getMessage());
            return;
        }

        QUESTIONS.put(chatId, textQuestion.answer());
    }

    @Override
    public boolean answerToQuestion(String chatId, String answer) {
        boolean isCorrectAnswer = false;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (!QUESTIONS.containsKey(chatId)) {
            LOGGER.info("Does not contains chat id");
            return isCorrectAnswer;
        }

        if (QUESTIONS.get(chatId).equals(answer)) {
            sendMessage.setText("Правильный ответ!");
            isCorrectAnswer = true;
        } else {
            sendMessage.setText(
                    "К сожалению, ответ неправильный. Ответ: "
                    + QUESTIONS.get(chatId));
        }

        try {
            BOT.execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.info(e.getMessage());
            return isCorrectAnswer;
        }

        QUESTIONS.remove(chatId);

        return isCorrectAnswer;
    }
}
