import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Answer {
    private HashMap<String, Association> mapAssociation = new HashMap<>();
    private final Random rand = new Random();

    public Answer(String filePath){
        readAssociations(filePath);
    }

    private void readAssociations(String filePath) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(filePath);

            // Просматриваем все подэлементы корневого - т.е. книги
            NodeList phrases =  doc.getElementsByTagName("phrase");
            for (int i = 0; i < phrases.getLength(); i++) {

                Node phrase = phrases.item(i);
                String word = phrase.getParentNode().getAttributes().getNamedItem("word").getNodeValue();
                if (!mapAssociation.containsKey(word)) {
                    this.mapAssociation.put(word, new Association(word));
                }

                NamedNodeMap attrs = phrase.getAttributes();
                String value = attrs.getNamedItem("value").getNodeValue();
                double weight = Double.parseDouble(attrs.getNamedItem("weight").getNodeValue());
                mapAssociation.get(word).addPhrase(value, weight);
            }

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private String[] textPreparation(String text) {
        text = text.replaceAll("\\p{P}", "");
        return text.split(" ");
    }

    private String toTitle(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public String getRandomAnswer(String text) {
        String[] listWords = textPreparation(text);
        int num = rand.nextInt(listWords.length);
        String answer = listWords[num];
        return String.format("%s?", this.toTitle(answer));
    }

    public String getWeightedAnswer(String text) {
        String[] wordList = textPreparation(text);
        List<String> filtered = Arrays.stream(wordList).filter(word -> mapAssociation.containsKey(word))
                .collect(Collectors.toList());
        switch (filtered.size()){
            case 0: return getRandomAnswer(text);
            case 1: return this.toTitle(mapAssociation.get(filtered.get(0)).getPhrase());
            default:
                int index = rand.nextInt(filtered.size());
                return this.toTitle(mapAssociation.get(filtered.get(index)).getPhrase());
        }
    }



}
