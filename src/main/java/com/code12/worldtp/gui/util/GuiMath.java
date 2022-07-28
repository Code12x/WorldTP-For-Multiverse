package com.code12.worldtp.gui.util;

import java.util.List;

public class GuiMath {
    /**
     *
     * @param pos
     * @param columns
     * @return Returns a List<Integer> with two Integers. The first integer is the x value and the second is the y value.
     */
    public static List<Integer> cordsFromPosition(int pos, int columns){
        int y = pos/columns;
        int x = pos - columns*y;

        return List.of(x, y);
    }
}
