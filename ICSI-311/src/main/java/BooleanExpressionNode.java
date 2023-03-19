/*
    Used to represent simple boolean expressions of two mathematical expressions
    separated by a condition
 */
public class BooleanExpressionNode extends Node{
    private Node left;
    private Node right;
    private Token.type condition;

    public BooleanExpressionNode(Node left, Node right, Token.type condition) {
        this.left = left;
        this.right = right;
        this.condition = condition;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Token.type getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return left + " " + condition + " " + right;
    }
}
