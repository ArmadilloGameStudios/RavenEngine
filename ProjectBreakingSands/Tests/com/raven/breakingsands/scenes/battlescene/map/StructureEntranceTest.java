package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.util.math.Vector2i;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StructureEntranceTest {

    @AfterEach
    void tearDown() {
    }

    @Test
    void getEntrancePosition() {
        System.out.println("Testing: getEntrancePosition");

        // basic
        System.out.println("Test Basic");
        Vector2i pos = StructureEntrance.getEntrancePosition(
                0, 0, 1, 1);

        Assertions.assertEquals(0, pos.x);
        Assertions.assertEquals(1, pos.y);

        // 0
        System.out.println("Test 0");
        pos = StructureEntrance.getEntrancePosition(
                0, 5, 23, 17);

        Assertions.assertEquals(5, pos.x);
        Assertions.assertEquals(17, pos.y);

        // 1
        System.out.println("Test 1");
        pos = StructureEntrance.getEntrancePosition(
                1, 5, 23, 17);

        Assertions.assertEquals(23, pos.x);
        Assertions.assertEquals(12, pos.y);

        // 2
        System.out.println("Test 2");
        pos = StructureEntrance.getEntrancePosition(
                2, 5, 23, 17);

        Assertions.assertEquals(18, pos.x);
        Assertions.assertEquals(0, pos.y);

        // 3
        System.out.println("Test 3");
        pos = StructureEntrance.getEntrancePosition(
                3, 5, 23, 17);

        Assertions.assertEquals(0, pos.x);
        Assertions.assertEquals(5, pos.y);
    }

    @Test
    void isConnected() {
        System.out.println("Testing: isConnected");
        Vector2i a, b, o;

        // basic
        System.out.println("Test Basic");
        a = StructureEntrance.getEntrancePosition(
                0, 5, 23, 18);
        b = StructureEntrance.getEntrancePosition(
                2, 1, 8, 6);

        o = StructureEntrance.getEntranceOffset(0, 1, a, b);

        System.out.println(o);

        b.x += o.x;
        b.y += o.y;

        Assertions.assertTrue(StructureEntrance.isConnected(0, 1, a, b));


    }
}