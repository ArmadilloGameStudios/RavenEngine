package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.graphics2d.shader.TextShader;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.engine2d.util.math.Vector4f;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UITextWriter {

    private SpriteSheet alphabetImage, alphabetSmallImage;

    private UIFont font;
    private UITexture uiImage;
    private UITextColorFeed colorFeed;
    private Iterator<Highlight> colorFeedIterator;
    private GameEngine engine;
    private String text, background;
    private UITextWriterHandler handler;

    private int x = 0; // 8
    private int y = 0; // 10
    private int lines = 0;
    private Vector2i
            size = new Vector2i(),
            src = new Vector2i(),
            des = new Vector2i();


    public UITextWriter(GameEngine engine, Scene scene) {
        this.engine = engine;

        alphabetImage = engine.getSpriteSheet("alphabet.png");
        alphabetImage.load(scene);
        alphabetSmallImage = engine.getSpriteSheet("alphabet_small.png");
        alphabetSmallImage.load(scene);
    }

    public void setBackground(String src) {
        background = src;
    }

    public void setImageDest(UITexture image) {
        uiImage = image;
    }

    public void setText(String text, UIFont font) {
        this.text = text;
        this.font = font;
    }

    public void setColorFeed(UITextColorFeed colorFeed) {
        this.colorFeed = colorFeed;
    }

    public void setHandler(UITextWriterHandler handler) {
        this.handler = handler;
    }

    public void clearHandler() {
        handler = null;
    }

    public void write(TextShader shader) {

        if (colorFeed == null)
            colorFeedIterator = UITextColorFeed.defaultIterator();
        else
            colorFeedIterator = colorFeed.iterator();

        x = 0; // 8
        y = 0; // 10
        lines = 0;

        shader.setWriteDestination(uiImage);

        if (background != null) {
            shader.drawImage(engine.getSpriteSheet(background));
        } else {
            shader.clear();
        }

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

            Character lastChar = null;
            char[] chars = text.toLowerCase().toCharArray();

            for (int index = 0; index < chars.length; index++) {
                char c = chars[index];

                if (font.isWrap()) {
                    // get next word length, if total is greater than width, new line;
                    if (c == '\n') {
                        x = font.getX();
                        y += 10;

                        lines += 1;
                    } else if (lastChar != null &&
                            !(Character.isAlphabetic(lastChar) || Character.isDigit(lastChar)) &&
                            (Character.isAlphabetic(c) || Character.isDigit(c))) {

                        int next = nextWordLength(index, chars, alphabetLocation);

                        if (next + x >= uiImage.getWidth()) {

                            x = font.getX();
                            y += 10;

                            lines += 1;
                        }

                    }
                }

                writeChar(shader, c, alphabetLocation);

                lastChar = c;
            }
        } else {
            x = uiImage.getWidth() - 1;

            char[] chars = new StringBuilder(text.toLowerCase()).reverse().toString().toCharArray();

            for (int index = 0; index < chars.length; index++) {
                char c = chars[index];
                writeChar(shader, c, alphabetLocation);
            }
        }

        if (handler != null) {
            handler.onFinish(lines + 1);
        }
    }

    private int nextWordLength(int i, char[] chars, GameDataList alphabetLocation) {
        AtomicInteger wordLen = new AtomicInteger();


        while (i < chars.length && (Character.isAlphabetic(chars[i]) || Character.isDigit(chars[i]))) {

            char c = chars[i];
            alphabetLocation.stream()
                    .filter(a -> a.getString("name").equals(Character.toString(c)))
                    .findFirst()
                    .ifPresent(a -> wordLen.addAndGet(a.getInteger("width") - 1));

            i++;
        }

        return wordLen.get();
    }

    private void writeChar(TextShader shader, Character c, GameDataList alphabetLocation) {
        engine.getWindow().printErrors("meow");

        Highlight highlight = colorFeedIterator.next();

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

                des.x = x;
                des.y = y;

                if (optionalgdChar.isPresent()) {
                    GameData gdChar = optionalgdChar.get();

                    int cx = gdChar.getInteger("x");
                    int cw = gdChar.getInteger("width");

                    size.x = cw;

                    if (!font.isSmall()) {

                        size.y = 14;

                        if (font.getSide() == UIFont.Side.RIGHT) {
                            x -= cw;
                        }

                        if (font.isButton()) {

                            des.x = x;
                            des.y = y;
                            src.x = cx;
                            src.y = 0;
                            shader.write(size, src, des, alphabetImage, highlight);

                            Vector2i o = font.getButtonOffset();
                            des.x += o.x;
                            des.y += o.y;
                            src.y = 14;
                            shader.write(size, src, des, alphabetImage, highlight);
                        } else {
                            if (font.isHighlight()) {

                                des.x = x;
                                des.y = y;
                                src.x = cx;
                                src.y = 14;
                                shader.write(size, src, des, alphabetImage, highlight);
                            } else {

                                des.x = x;
                                des.y = y;
                                src.x = cx;
                                src.y = 0;
                                shader.write(size, src, des, alphabetImage, highlight);
                            }
                        }

                        if (font.getSide() == UIFont.Side.LEFT) {
                            x += cw;
                        }
                    } else {

                        size.y = 9;

                        if (font.getSide() == UIFont.Side.RIGHT) {
                            x -= cw - 1;
                        }

                        if (font.isButton()) {

                            des.x = x;
                            des.y = y;
                            src.x = cx;
                            src.y = 0;
                            shader.write(size, src, des, alphabetSmallImage, highlight);

                            Vector2i o = font.getButtonOffset();
                            des.x += o.x;
                            des.y += o.y;
                            src.y = 9;
                            shader.write(size, src, des, alphabetSmallImage, highlight);
                        } else {

                            des.x = x;
                            des.y = y;
                            src.x = cx;
                            src.y = 9;
                            shader.write(size, src, des, alphabetSmallImage, highlight);
                        }

                        if (font.getSide() == UIFont.Side.LEFT) {
                            x += cw - 1;
                        }
                    }
                }
                break;
        }

    }
}