/*
    Used to represent assignment of expressions to a variable name
 */
public class AssignmentNode extends statementNode{
    private VariableReferenceNode target;
    private Node expression;

    public AssignmentNode(VariableReferenceNode target, Node expression) {
        this.target = target;
        this.expression = expression;
    }

    public VariableReferenceNode getTarget() {
        return target;
    }

    public Node getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return target.toString() + " assigned " + expression.toString();
    }
}
