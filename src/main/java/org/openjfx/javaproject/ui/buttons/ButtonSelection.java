package org.openjfx.javaproject.ui.buttons;

import org.openjfx.javaproject.common.EntityEnum;

public class ButtonSelection {
    private EntityEnum mode = EntityEnum.NONE;

    public EntityEnum getMode() {
        return mode;
    }

    public void setMode(EntityEnum mode) {
        this.mode = mode;
    }
}
