import java.util.List;
/*
    Used to represent the repeat control structure
    Has statements and then a boolean expression - reverse of while loop
 */
public class RepeatNode extends statementNode{

    private Node booleanExpression;
    private List<statementNode> statementNodeList;

    public RepeatNode(Node booleanExpression, List<statementNode> statementNodeList) {
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
        String result = "Repeat Loop \n          Boolean Expression: " + booleanExpression + "\n          Statements: \n";
        for(statementNode statementNode : statementNodeList) {
            result += "               " + statementNode + "\n";
        }
        return result;
    }
}
