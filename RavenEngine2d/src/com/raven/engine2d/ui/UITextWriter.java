package com.raven.engine2d.ui;

import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;

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
    private UIFont font;
    private UITexture uiImage;

    public UITextWriter(UITexture image, UIFont font) {
        uiImage = image;
        img = image.getImage();
        this.font = font;
    }

    // Will overwrite any text on the image
    public void drawBackground(String src) {
        uiImage.drawImage(src);
    }


    public void write(String text) {
        if (imgGraphics == null)
            imgGraphics = img.createGraphics();

        GameDataList alphabetLocation;
        if (font.isSmall())
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

        for (Character c : text.toLowerCase().toCharArray()) {
            writeChar(c, alphabetLocation, font.isSmall());
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
                                cx, 8, cx + cw, 16,
                                null);

                        x += cw - 1;
                    }
                }
                break;
        }

    }

    public void clear() {
        if (imgGraphics == null)
            imgGraphics = img.createGraphics();

        System.out.println("clear");

        imgGraphics.setBackground(new Color(255, 255, 255, 0));
        imgGraphics.clearRect(0, 0, img.getWidth(), img.getHeight());
    }
}
