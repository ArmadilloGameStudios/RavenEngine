package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class UITextWriter {

    private static BufferedImage alphabetImage, alphabetSmallImage;

    {
        try {
            alphabetImage = ImageIO.read(new File(
                    GameProperties.getMainDirectory() + File.separator +
                            "text" + File.separator +
                            "alphabet.png"));

            alphabetSmallImage = ImageIO.read(new File(
                    GameProperties.getMainDirectory() + File.separator +
                            "text" + File.separator +
                            "alphabet_small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage img;
    private Graphics2D imgGraphics;

    public UITextWriter(UIImage image) {
        img = image.getImage();
    }

    // Will overwrite any text on the image
    public void drawBackground(String src) {
        SpriteSheet background = GameEngine.getEngine().getSpriteSheet(src);

        imgGraphics = img.createGraphics();
        imgGraphics.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        imgGraphics.drawImage(background.getImage(), 0, 0, null);
    }


    public void write(String text, boolean small) {
        imgGraphics = img.createGraphics();
//        imgGraphics.setComposite(
//                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        GameDataList alphabetLocation;
        if (small)
            alphabetLocation = GameDatabase.all("text").stream()
                    .filter(d -> d.getString("name").equals("alphabet small"))
                    .findFirst()
                    .map(d -> d.getList("chars"))
                    .get();
        else
            alphabetLocation = GameDatabase.all("text").stream()
                    .filter(d -> d.getString("name").equals("alphabet"))
                    .findFirst()
                    .map(d -> d.getList("chars"))
                    .get();

        for (Character c : text.toCharArray()) {
            writeChar(c, alphabetLocation, small);
        }

    }

    private int x = 8;
    private int y = 10;

    private void writeChar(Character c, GameDataList alphabetLocation, boolean small) {

        switch (c) {
            case ' ':
                if (small)
                    x += 3;
                else
                    x += 6;
                break;
            default:
                Optional<GameData> optionalgdChar = alphabetLocation.stream()
                        .filter(a -> a.getString("name").equals(c.toString()))
                        .findFirst();

                if (optionalgdChar.isPresent()) {
                    GameData gdChar = optionalgdChar.get();

                    int cx = gdChar.getInteger("x");
                    int cw = gdChar.getInteger("width");

                    if (!small) {
                        imgGraphics.drawImage(alphabetImage,
                                x, y, x + cw, y + 14,
                                cx, 0, cx + cw, 14,
                                null);

                        imgGraphics.drawImage(alphabetImage,
                                x, y + 32, x + cw, y + 32 + 14,
                                cx, 14, cx + cw, 28,
                                null);

                        x += cw;
                    } else {
                        imgGraphics.drawImage(alphabetSmallImage,
                                x, y, x + cw, y + 8,
                                cx, 0, cx + cw, 8,
                                null);

                        imgGraphics.drawImage(alphabetSmallImage,
                                x, y + 32, x + cw, y + 32 + 8,
                                cx, 8, cx + cw, 16,
                                null);

                        x += cw - 1;
                    }
                }
                break;
        }

    }

}
