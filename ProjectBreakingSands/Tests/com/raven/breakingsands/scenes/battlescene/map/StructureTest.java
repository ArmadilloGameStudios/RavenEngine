package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class StructureTest {

    @Test
    public void overlapTest() {
        // bottom left
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, -1, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, -1, 1, 2));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, -1, 2, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                -1, -1, 2, 2));

        // upper left
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, 14, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, 14, 1, 2));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                -1, 14, 2, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, 15, 2, 1));

        // upper right
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, 15, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                9, 15, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, 14, 1, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                9, 14, 1, 1));

        // bottom right
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, -1, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, -1, 2, 2));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                9, -1, 1, 2));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                9, -1, 2, 1));

        // left
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, 5, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -1, 5, 1, 2));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                -1, 5, 2, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                -1, 5, 2, 2));

        // right
        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                9, 5, 1, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                9, 5, 2, 2));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, 5, 2, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, 5, 2, 2));

        // bottom
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                5, -1, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                5, -1, 2, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                5, -1, 1, 2));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                5, -1, 2, 2));

        // top
        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                5, 15, 1, 1));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                5, 15, 2, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                5, 14, 2, 1));

        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                5, 14, 2, 2));

        // full left
        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                -2, -2, 4, 20));


        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                -4, -2, 4, 20));

        // full right
        Assertions.assertTrue(Structure.overlaps(
                0, 0, 10, 15,
                8, -2, 4, 20));

        Assertions.assertFalse(Structure.overlaps(
                0, 0, 10, 15,
                10, -2, 4, 20));

        // full bottom

//        Rectangle2D c = new Rectangle2D(1,1,1,1);
//        c.intersects()
    }

}
