package parser.idParser.controller;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DetectText {
    public static void detectText() throws IOException {
        String filePath = "X:\\Work Projects\\Java Siit\\DocParser\\src\\main\\resources\\static\\cristiBuletinSmall.jpg";
        detectText(filePath);
    }

    public static Object detectText(String filePath) throws IOException{
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ArrayList<String> docData= new ArrayList<>();
        VisionData visionData = new VisionData();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    visionData.setError(res.getError().getMessage());
                    return docData;
                }

                int index = 0;
                visionData.setTextAnnotation(res.getFullTextAnnotation());
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    docData.add(annotation.getDescription());
                    index++;
                }
            }

            return visionData;
        }
    }

    public static UserData parseDataForUser(String filePath) {
        VisionData visionData = new VisionData();
        try {
            visionData = (VisionData) detectText(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(visionData.getError() != null) {
            String errorMessage = visionData.getError();
        }

        DocumentData documentData = new DocumentData(visionData.getTextAnnotation());
        ArrayList <String> wordsList = documentData.getWordListText();
        Map<String, Integer> wordsWithConfidence = documentData.getWordsWithConfidence();

        UserData userData = new UserData();
        userData.setDocumentData(documentData);

        int cnpKey = wordsList.indexOf("CNP");
        if(cnpKey >= 0){
            String cnp = wordsList.get(cnpKey + 1);
            userData.setCnp(cnp);
            userData.setCnpConf(wordsWithConfidence.get(cnp));
        }

        int lastNameKey = -1;
        int firstNameKey = -1;
        int addressKey = -1;
        int issuedKey = -1;

        int dataIndex = 0;
        for(String word : wordsList) {
//            Todo:: add word to lowercase. And use lowercase everywhere.
            if(word.contains("Last") && wordsList.get(dataIndex + 1).contains("name")){
                lastNameKey = dataIndex + 2;
            }
            if(word.contains("First") && wordsList.get(dataIndex + 1).contains("name")){
                firstNameKey = dataIndex + 2;
            }
            if(word.contains("Domiciliu") || word.contains("Adresse") || word.contains("Address")){
                addressKey = dataIndex;
            }

            if(word.contains("Emis")) {
                issuedKey = dataIndex;
            }
            dataIndex++;
        }


        if(lastNameKey >= 0){
            String lastName = wordsList.get(lastNameKey);
            userData.setLastName(lastName);
            userData.setLastNameConf(wordsWithConfidence.get(lastName));
        }

        if(firstNameKey >= 0){
            String firstName = wordsList.get(firstNameKey);
            userData.setFirstName(firstName);
            userData.setFirstNameConf(wordsWithConfidence.get(firstName));
        }

        StringBuilder address;
        if(addressKey > 0 && issuedKey > 0){
            address = new StringBuilder();
            float minAddressConf = 0.9F;
            for(int i = addressKey+1; i < issuedKey; i++){
                address.append(wordsList.get(i)).append(" ");
                float currentWordConfidence = wordsWithConfidence.get(wordsList.get(i));
                if(minAddressConf > currentWordConfidence){
                    minAddressConf = currentWordConfidence;
                }
            }

            if(address.toString().contains("Sex/Sexe/Sex")){
                address = new StringBuilder(address.toString().replace("Sex/Sexe/Sex", ""));
            }
            userData.setAddress(address.toString());
            userData.setAddressConf(minAddressConf);
        }

        return userData;
    }

    /**
     * Main function used only for testing development.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String filePath = "X:\\Work Projects\\Java Siit\\DocParser\\src\\main\\resources\\static\\cristiScan.jpg";
        VisionData visionData = (VisionData) detectText(filePath);
        System.out.println("id data: ");
        System.out.println(visionData);
        if(visionData.getError() == null){
            DocumentData docData = new DocumentData(visionData.getTextAnnotation());
            List<Paragraph> paragraphs = docData.getParagraphList();
//            System.out.println("%nParagraphs %n" + paragraphs);
            for (Paragraph paragraph : paragraphs) {
                String paragraphText = "";
            }

            ArrayList<String> words = docData.getWordListText();
            HashMap<String, Integer> wordsWithConfidence = docData.getWordsWithConfidence();

            ArrayList<Word> wordsObj = docData.getWordList();
            float minConfidence = 1f;
            String minWord = "";
            for (Word word : wordsObj) {
                float confidence = word.getConfidence();
                if(confidence < minConfidence) {
                    minConfidence = confidence;
                    minWord = docData.getWordText(word);
                }
            }


            System.out.println(" this are the words: " + words);
            System.out.println("this are the words and their confidence: " + wordsWithConfidence);
            System.out.println("'Last' confidence: " + docData.getConfidence("Last"));
            System.out.println(" Min confidence: " + docData.getMinConfidence());
            System.out.println(" min confidence word: " + minWord);
        } else {
            System.out.println("there is and error.");
        }

        TextAnnotation annotation = visionData.getTextAnnotation();
    }
}

