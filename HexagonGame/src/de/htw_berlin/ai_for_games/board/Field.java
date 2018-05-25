package de.htw_berlin.ai_for_games.board;

public class Field {
    public int x;
    public int y;

    public Field(Field srcField) {
        this.x = srcField.x;
        this.y = srcField.y;
    }

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // self check
        }
        if (o == null) {
            return false; // null check
        }
        if (!(o instanceof Field)) {
            return false; // type check
        }

        Field field = (Field) o;
        return this.x == field.x && this.y == field.y; // member comparison
    }

    @Override
    public int hashCode() {
        return this.x * 31 + this.y;
    }
}