package com.armadillogamestudios.storyteller.stringformatter;

import com.armadillogamestudios.engine2d.ui.UITextColorFeed;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.resource.Resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    private static Pattern r = Pattern.compile("(\\<[^\\>]+\\>)");
    private static Pattern t = Pattern.compile("[0-9]+");
    private StringFormatter() {
    }

    public static StringColored format(String toFormat, Resource[] targets) {
        Matcher matcher = r.matcher(toFormat);

        if (matcher.find()) {
            String[] split = r.split(toFormat);
            StringBuilder stringBuilder = new StringBuilder(split[0]);
            UITextColorFeed.Builder colorBuilder = new UITextColorFeed.Builder();

            colorBuilder.next(split[0].length(), new Highlight(1, 1, 1, 1));

            int sIndex = 0;

            do {
                String group = matcher.group();
                group = group.substring(1, group.length() - 1);

                Matcher targetMatcher = t.matcher(group);
                targetMatcher.find();

                int target = Integer.parseInt(targetMatcher.group());

                char type = group.charAt(targetMatcher.end());

                switch (type) {
                    case '?': // has prop
                    case '!': // not has prop
                    case '#': // read prop
                    default:
                        String[] traitProp = group.substring(targetMatcher.end() + 1).split(":");

                        String text = targets[target]
                                .getTrait(traitProp[0])
                                .getProp(traitProp[1]);

                        stringBuilder.append(text);
                        colorBuilder.next(text.length(), new Highlight(0, 0, 1, .9f));
                        break;
                }

                sIndex++;

                stringBuilder.append(split[sIndex]);
                colorBuilder.next(split[sIndex].length(), new Highlight(1, 1, 1, 1));

            } while (matcher.find());

            return new StringColored(stringBuilder.toString(), colorBuilder.build());
        } else {
            return new StringColored(toFormat);
        }
    }
}
