import java.util.List;
/*
    Represents the if control structure
    Can hold nested for and else statements
 */
public class IfNode extends statementNode{
    private Node booleanExpression;
    private List<statementNode> statementNodesList;
    private IfNode next;

    public IfNode(Node booleanExpression, List<statementNode> statementNodesList, IfNode ifNode) {
        this.booleanExpression = booleanExpression;
        this.statementNodesList = statementNodesList;
        this.next = ifNode;
    }

    public Node getBooleanExpression() {
        return booleanExpression;
    }

    public List<statementNode> getStatementNodesList() {
        return statementNodesList;
    }

    public IfNode getNext() {
        return next;
    }

    @Override
    public String toString() {
        String result = "If Statement\n";
        result += "     Boolean Expression: " + booleanExpression + "\n     Statements: \n";
        for (statementNode statementNode : statementNodesList) {
            result += "          " + statementNode + "\n";
        }
        if (next != null) {
            if(next.getBooleanExpression() != null) {
                result += "     elsif\n";
            }
            result += "     " + next;
        }
        return result;
    }
}
