package org.textrecognizer.textdetector;

import com.google.mlkit.vision.text.Text;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextConverter {

    public static String getTextLeftToRight(final Text text) {
        return getElementStream(text)
                .sorted(Comparator.comparingInt(element -> element.getBoundingBox().left))
                .map(Text.Element::getText)
                .collect(Collectors.joining());
    }

    private static Stream<Text.Element> getElementStream(final Text text) {
        return text.getTextBlocks().stream().flatMap(TextConverter::getElementStream);
    }

    private static Stream<Text.Element> getElementStream(final Text.TextBlock textBlock) {
        return textBlock.getLines().stream().flatMap(TextConverter::getElementStream);
    }

    private static Stream<Text.Element> getElementStream(final Text.Line line) {
        return line.getElements().stream();
    }
}
