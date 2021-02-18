package parser.idParser.controller;

import com.google.cloud.vision.v1.TextAnnotation;

public class VisionData {
    private String error;
    private TextAnnotation textAnnotation;

    public VisionData(){

    }

    public VisionData(String error) {
        this.error = error;
    }

    public VisionData(TextAnnotation textAnnotation) {
        this.textAnnotation = textAnnotation;
    }

    public String getError() {
        return error;
    }

    public TextAnnotation getTextAnnotation() {
        return textAnnotation;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTextAnnotation(TextAnnotation textAnnotation) {
        this.textAnnotation = textAnnotation;
    }
}
