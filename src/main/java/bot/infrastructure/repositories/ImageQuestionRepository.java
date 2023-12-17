package bot.infrastructure.repositories;

import bot.models.ImageQuestion;

import java.io.File;
import java.util.Random;

public class ImageQuestionRepository {
    private final File PATH = new File(System.getenv("PICTURES_DIRECTORY"));
    private final Random RANDOM_GENERATOR = new Random();
    public ImageQuestion getRandomQuestion() {
        File[] images = PATH.listFiles();
        int randomIndex = RANDOM_GENERATOR.nextInt(images.length);

        File photo = images[randomIndex];

        return new ImageQuestion("Кто изображён на этом фото?",
                photo.getName().split("\\.")[0],
                photo);
    }
}
