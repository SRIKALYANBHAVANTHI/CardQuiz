package com.example.cardquiz;

// QuizParser.java
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// Parses quiz questions from an XML file
public class QuizParser {
    // Parses the XML input stream and returns a list of quiz questions
    public static List<QuizQuestion> parseQuestions(InputStream inputStream) {
        List<QuizQuestion> questions = new ArrayList<>();

        try {
            // Create XMLPullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            // Parse XML
            int eventType = parser.getEventType();
            String questionText = null;
            List<String> options = null;
            List<String> correctAnswers = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("question".equals(tagName)) {
                            options = new ArrayList<>();
                            correctAnswers = new ArrayList<>();
                        } else if ("text".equals(tagName)) {
                            parser.next();
                            questionText = parser.getText();
                        } else if ("option".equals(tagName)) {
                            parser.next();
                            options.add(parser.getText());
                        } else if ("answer".equals(tagName)) {
                            parser.next();
                            correctAnswers.add(parser.getText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("question".equals(tagName)) {
                            // Create QuizQuestion object and add to list
                            QuizQuestion question = new QuizQuestion(questionText, options, correctAnswers);
                            questions.add(question);
                        }
                        break;
                }

                // Move to next event
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
}

