package com.joyfulmagic.colors.views.Checkable;

/**
 * Simple checked int
 */
public class CheckedInt {

    private int number; // value of number
    private boolean state; // checked or not

    public CheckedInt(int number, boolean state) {
        this.number = number;
        this.state = state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
