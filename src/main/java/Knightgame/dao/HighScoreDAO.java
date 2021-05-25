package Knightgame.dao;


import net.bytebuddy.implementation.bytecode.member.FieldAccess;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The HighScoreDAO implements the methods to read and white XML file.
 */
public class HighScoreDAO {
    public String path = "src\\main\\resources\\highscore.xml";
    /**
     * Add to score to XML file.
     *
     * @param score Score.
     */
    public void addScore(Score score){
        HighScore highScore = getHighScores();
        highScore.addScore(score);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HighScore.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            File file = new File(path);
            OutputStream outputStream = new FileOutputStream(path);
            marshaller.marshal(highScore,file);
            marshaller.marshal(highScore,System.out);
            System.out.println(outputStream);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the elements of the XML file.
     *
     * @return Highscore list.
     */
    public HighScore getHighScores(){
        HighScore highScore = new HighScore();
        try {
            JAXBContext jaxbContext =JAXBContext.newInstance(HighScore.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            //InputStream inputStream = getClass().getResourceAsStream("highscore.xml");
            InputStream inputStream = new FileInputStream(path);
            highScore = (HighScore) unmarshaller.unmarshal(inputStream);
            inputStream.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }
}