package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UITextWriter {

    private static BufferedImage alphabet;

    {
        try {
            alphabet = ImageIO.read(new File(
                    GameProperties.getMainDirectory() + File.separator +
                            "text" + File.separator +
                            "alphabet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UITextWriter() {

    }

    public static void write(UIImage image, String text) {
        BufferedImage img = image.getImage();

        GameDataList alphabetLocation = GameDatabase.all("text").stream()
                .filter(d -> d.getString("name").equals("alphabet"))
                .findFirst()
                .map(d -> d.getList("chars"))
                .get();

        // TODO draw the text on the img
    }

}
