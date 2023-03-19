/*
    Used to represent complex mathematical expressions,
    nodes can contain mathopnodes or different data types
 */
public class MathOpNode extends Node{

    private final operator instanceType;
    private final Node left;
    private final Node right;

    public MathOpNode(operator operator, Node left, Node right) {
        this.instanceType = operator;
        this.left = left;
        this.right = right;
    }

    public enum operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, mod, lessThan, greaterThan, lessThanEqualTo, greaterThanEqualTo, equalTo, notEqual
    }


    public operator getInstanceType() {
        return instanceType;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public enum type {
        TIMES, MINUS, DIVIDE, PLUS
    }

    @Override
    public String toString() {

        return getLeft() + " " + getInstanceType() + " " + getRight();
    }
}
