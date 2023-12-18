package bot.gamemode;


public interface IGameMode {
    void askQuestion(String chatId);
    boolean answerToQuestion(String chatId, String answer);
}
