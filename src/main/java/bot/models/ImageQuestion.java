package bot.models;

import java.io.File;

public record ImageQuestion(String question, String answer, File photo) {
}
