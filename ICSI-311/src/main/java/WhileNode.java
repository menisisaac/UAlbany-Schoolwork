import java.util.List;
/*
    Represents the while control structure,
    holds statements and a boolean expression
 */
public class WhileNode extends statementNode{
    Node booleanExpression;
    List<statementNode> statementNodeList;

    public WhileNode(Node booleanExpression, List<statementNode> statementNodeList) {
        this.booleanExpression = booleanExpression;
        this.statementNodeList = statementNodeList;
    }

    public Node getBooleanExpression() {
        return booleanExpression;
    }

    public List<statementNode> getStatementNodeList() {
        return statementNodeList;
    }


    @Override
    public String toString() {
        String result = "While Loop \n          Boolean Expression: " + booleanExpression + "\n          Statements: \n";
        for(statementNode statementNode : statementNodeList) {
            result += "               " + statementNode + "\n";
        }
        return result;
    }
}
