package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.worldobject.Highlight;

import java.util.*;

public class UITextColorFeed implements Iterable<Highlight> {

    private List<Map.Entry<Integer, Highlight>> info;

    private UITextColorFeed(List<Map.Entry<Integer, Highlight>> info) {
        this.info = info;
    }

    public static Iterator<Highlight> defaultIterator() {
        return new Iterator<Highlight>() {

            Highlight color = new Highlight(0, 0, 0, 0);

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Highlight next() {
                return color;
            }
        };
    }

    @Override
    public Iterator<Highlight> iterator() {
        return new UITextColorFeedIterator(info);
    }

    public static class Builder {
        private List<Map.Entry<Integer, Highlight>> info = new ArrayList<>();
        private int count;
        private Highlight lastColor;

        public void next(Highlight color) {
            if (lastColor == null) {
                count = 1;
                lastColor = color;
            } else if (lastColor == color) {
                count += 1;
            } else {
                info.add(new AbstractMap.SimpleImmutableEntry<>(count, lastColor));
                count = 1;
                lastColor = color;
            }
        }

        public void next(int length, Highlight color) {
            if (lastColor == null) {
                count = length;
                lastColor = color;
            } else if (lastColor == color) {
                count += length;
            } else {
                info.add(new AbstractMap.SimpleImmutableEntry<>(count, lastColor));
                count = length;
                lastColor = color;
            }
        }

        public UITextColorFeed build() {
            info.add(new AbstractMap.SimpleImmutableEntry<>(count, lastColor));
            return new UITextColorFeed(info);
        }
    }

    private static class UITextColorFeedIterator implements Iterator<Highlight> {

        private List<Map.Entry<Integer, Highlight>> info;
        private int infoIndex, colorIndex;

        UITextColorFeedIterator(List<Map.Entry<Integer, Highlight>> info) {
            this.info = info;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Highlight next() {
            try {
                if (colorIndex >= info.get(infoIndex).getKey()) {
                    infoIndex += 1;
                    colorIndex = 0;
                }

                colorIndex += 1;

                return info.get(this.infoIndex).getValue();
            } catch (Exception e) {
                return new Highlight(0, 0, 0, 1);
            }
        }
    }
}
