/*
    Holds the Character data structure
 */
public class CharNode extends Node{
    private char value;
    public CharNode(String c) {
        this.value = c.charAt(0);
    }
    public char getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Char: " + value;
    }
}
