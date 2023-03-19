import java.util.List;
/*
    Represents the else control structure in a if loop
 */
public class ElseNode extends IfNode{
    public ElseNode(List<statementNode> statementNodesList) {
        super(null, statementNodesList, null);
    }

    @Override
    public String toString() {
        String result = "     Else:\n     Statements: \n";
        for(statementNode statementNode : super.getStatementNodesList()) {
            result += "          " + statementNode;
        }
        return result;
    }
}
