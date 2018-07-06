package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.util.math.Vector2i;
import org.lwjgl.system.CallbackI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    private GameEngine engine;

    private int x = 0; // 8
    private int y = 0; // 10

    public UITextWriter(GameEngine engine, UITexture image, UIFont font) {
        this.engine = engine;

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
            alphabetLocation = engine.getGameDatabase().getTable("text").stream()
                    .filter(d -> d.getString("name").equals("alphabet small"))
                    .findFirst()
                    .map(d -> d.getList("chars"))
                    .get();
        else
            alphabetLocation = engine.getGameDatabase().getTable("text").stream()
                    .filter(d -> d.getString("name").equals("alphabet"))
                    .findFirst()
                    .map(d -> d.getList("chars"))
                    .get();

        x = font.getX();
        y = font.getY();

        if (font.getSide() == UIFont.Side.LEFT) {
            for (Character c : text.toLowerCase().toCharArray()) {
                writeChar(c, alphabetLocation);
            }
        } else {
            x = img.getWidth() - 1;

            for (Character c : new StringBuilder(text.toLowerCase()).reverse().toString().toCharArray()) {
                writeChar(c, alphabetLocation);
            }
        }
    }

    private void writeChar(Character c, GameDataList alphabetLocation) {

        switch (c) {
            case ' ':
                if (font.isSmall())
                    if (font.getSide() == UIFont.Side.RIGHT)
                        x -= 3;
                    else
                        x += 3;
                else {
                    if (font.getSide() == UIFont.Side.RIGHT)
                        x -= 6;
                    else
                        x += 6;
                }
                break;
            default:
                Optional<GameData> optionalgdChar = alphabetLocation.stream()
                        .filter(a -> a.getString("name").equals(c.toString()))
                        .findFirst();

                if (optionalgdChar.isPresent()) {
                    GameData gdChar = optionalgdChar.get();

                    int cx = gdChar.getInteger("x");
                    int cw = gdChar.getInteger("width");

                    if (!font.isSmall()) {
                        if (font.getSide() == UIFont.Side.RIGHT) {
                            x -= cw;
                        }

                        if (font.isButton()) {
                            imgGraphics.drawImage(alphabetImage,
                                    x, y, x + cw, y + 14,
                                    cx, 0, cx + cw, 14,
                                    null);

                            Vector2i o = font.getButtonOffset();

                            imgGraphics.drawImage(alphabetImage,
                                    x + o.x, y + o.y, x + cw + o.x, y + o.y + 14,
                                    cx, 14, cx + cw, 28,
                                    null);
                        } else {
                            if (font.isHighlight()) {
                                imgGraphics.drawImage(alphabetImage,
                                        x, y, x + cw, y + 14,
                                        cx, 14, cx + cw, 28,
                                        null);
                            } else {
                                imgGraphics.drawImage(alphabetImage,
                                        x, y, x + cw, y + 14,
                                        cx, 0, cx + cw, 14,
                                        null);
                            }
                        }

                        if (font.getSide() == UIFont.Side.LEFT) {
                            x += cw;
                        }
                    } else {
                        if (font.getSide() == UIFont.Side.RIGHT) {
                            x -= cw - 1;
                        }

                        if (font.isButton()) {
                            imgGraphics.drawImage(alphabetSmallImage,
                                    x, y, x + cw, y + 8,
                                    cx, 8, cx + cw, 16,
                                    null);

                            Vector2i o = font.getButtonOffset();

                            imgGraphics.drawImage(alphabetSmallImage,
                                    x + o.x, y + o.y, x + cw + o.x, y + o.y + 8,
                                    cx, 8, cx + cw, 16,
                                    null);
                        } else {
                            imgGraphics.drawImage(alphabetSmallImage,
                                    x, y, x + cw, y + 8,
                                    cx, 8, cx + cw, 16,
                                    null);
                        }

                        if (font.getSide() == UIFont.Side.LEFT) {
                            x += cw - 1;
                        }
                    }
                }
                break;
        }

    }

    public void clear() {
        if (imgGraphics == null)
            imgGraphics = img.createGraphics();

        imgGraphics.setBackground(new Color(255, 255, 255, 0));
        imgGraphics.clearRect(0, 0, img.getWidth(), img.getHeight());
    }
}
