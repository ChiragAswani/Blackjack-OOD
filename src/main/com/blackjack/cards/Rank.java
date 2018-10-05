package com.blackjack.cards;

public enum Rank {
    two (2), three (3), four (4), five (5), six (6), seven (7), eight (8), nine (9), ten (10),
    jack (12), queen (13), king (14), ace (11);

    public final int value;

    Rank(int value) {
        this.value = value;
    }
    public boolean isFace() {
        switch (value) {
            case 12:
            case 13:
            case 14:
                return true;
            default:
                return false;
        }
    }
}
