package bot.gamemode;

import bot.infrastructure.repositories.ImageQuestionRepository;
import bot.models.ImageQuestion;
import bot.models.TextQuestion;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ImageGameMode implements IGameMode {
    private final Logger LOGGER = Logger.getAnonymousLogger();
    private final TelegramLongPollingBot BOT;
    private final ImageQuestionRepository IMAGE_QUERY_REPOSITORY;
    private final Map<String, String> QUESTIONS = new HashMap<>();

    public ImageGameMode(TelegramLongPollingBot bot,
                         ImageQuestionRepository textQuestionRepository) {
        this.BOT = bot;
        this.IMAGE_QUERY_REPOSITORY = textQuestionRepository;
    }
    @Override
    public void askQuestion(String chatId) {
        ImageQuestion gameQuestion = IMAGE_QUERY_REPOSITORY.getRandomQuestion();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(gameQuestion.question());

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(gameQuestion.photo()));
        sendPhoto.setChatId(chatId);

        try {
            BOT.execute(sendMessage);
            BOT.execute(sendPhoto);
        } catch (TelegramApiException e) {
            LOGGER.info(e.getMessage());
            return;
        }

        QUESTIONS.put(chatId, gameQuestion.answer());
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
