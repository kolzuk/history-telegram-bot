package bot.gamemode;


public interface IGameMode {
    void askQuestion(String chatId);
    void answerToQuestion(String chatId, String answer);
}
