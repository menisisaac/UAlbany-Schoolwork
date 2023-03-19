import java.util.List;
/*
    Represent the for control structure
    Start, end, and variableReferenceNode should all be integers
 */
public class ForNode extends statementNode{
    private VariableReferenceNode variableReferenceNode;
    private Node start;
    private Node end;
    private List<statementNode> statementNodesList;

    public ForNode(VariableReferenceNode variableReferenceNode, Node start, Node end, List<statementNode> statementNodesList) {
        this.variableReferenceNode = variableReferenceNode;
        this.start = start;
        this.end = end;
        this.statementNodesList = statementNodesList;
    }

    public VariableReferenceNode getVariableReferenceNode() {
        return variableReferenceNode;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public List<statementNode> getStatementNodesList() {
        return statementNodesList;
    }

    @Override
    public String toString() {
        String result = "For Loop:\n        ";
        result += "From " + start + " to " + "finish\n        Statements\n";
        for(statementNode statementNode : statementNodesList) {
            result += "        " + statementNode + "\n";
        }
        return result;
    }
}
