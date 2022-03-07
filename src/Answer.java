import java.util.Random;

public class Answer {

    public String getRandomAnswer(String text) {
        text = text.replaceAll("\\p{P}", "");
        String [] listWords = text.split(" ");
        Random random = new Random();
        int num = random.nextInt(listWords.length);
        String answer = listWords[num];
        answer = answer.substring(0, 1).toUpperCase() + answer.substring(1);
        return String.format("%s?", answer);
    }
}
