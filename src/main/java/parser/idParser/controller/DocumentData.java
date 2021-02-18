package parser.idParser.controller;

import com.google.cloud.vision.v1.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DocumentData {
    private final TextAnnotation rawData;
    private ArrayList<Word> wordList = new ArrayList<>();
    private ArrayList<String> wordListText = new ArrayList<>();
    private ArrayList<Paragraph> paragraphList = new ArrayList<>();
    private ArrayList<Float> allConfidenceList = new ArrayList<>();
    private HashMap<String, Integer> wordsWithConfidence = new HashMap<String, Integer>();
    private String minConfidenceWord;
    private float minConfidence;

    public DocumentData(TextAnnotation textAnnotation) {
        this.rawData = textAnnotation;
        setParagraphList();
        setWordList();
        setWordListText();
        setWordsWithConfidence();
        setAllConfidenceList();
    }

    public void setParagraphList() {
        for (Page page : rawData.getPagesList()) {
            for (Block block : page.getBlocksList()) {
                paragraphList.addAll(block.getParagraphsList());
            }
        }
    }

    public List<Paragraph> getParagraphList() {
        return paragraphList;
    }

    public void setWordList() {
        for(Paragraph paragraph : paragraphList){
            wordList.addAll(paragraph.getWordsList());
        }
    }

    public void setWordsWithConfidence() {
        minConfidence = 1.0F;
        for(Word word : wordList) {
            String wordText = getWordText(word);
            float wordConfidence = word.getConfidence();
            if(wordConfidence < minConfidence) {
                minConfidence = wordConfidence;
                minConfidenceWord = wordText;
            }
            int wordConfidencePercentage = Math.round(wordConfidence * 100);
            wordsWithConfidence.put(wordText, wordConfidencePercentage);
        }
    }

    public void setAllConfidenceList() {
        for (Word word : wordList) {
            allConfidenceList.add(word.getConfidence());
        }
    }

    public ArrayList<Float> getAllConfidenceList() {
        return allConfidenceList;
    }

    public float getMinConfidence() {
        return minConfidence;
    }

    public HashMap<String, Integer> getWordsWithConfidence() {
        return wordsWithConfidence;
    }

    public void setWordListText() {
        for (Word word : wordList) {
            StringBuilder wordText = new StringBuilder();
            for (Symbol symbol : word.getSymbolsList()) {
                wordText.append(symbol.getText());
            }
            wordListText.add(wordText.toString());
        }
    }

    public String getWordText(Word word){
        StringBuilder wordText = new StringBuilder();
        for (Symbol symbol : word.getSymbolsList()) {
            wordText.append(symbol.getText());
        }
        return wordText.toString();
    }

    public ArrayList<String> getWordListText() {
        return wordListText;
    }

    public ArrayList<Word> getWordList() {
        return wordList;
    }

    public float getConfidence(String word) {
        return wordsWithConfidence.get(word);
    }

    public String getMinConfidenceWord() {
        return minConfidenceWord;
    }

    public TextAnnotation getRawData() {
        return rawData;
    }
}
