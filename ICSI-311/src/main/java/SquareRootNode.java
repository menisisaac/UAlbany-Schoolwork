import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Takes in an integer and the square root in the float data type
 */
public class SquareRootNode extends BuiltInFunctions{
    private static List<VariableNode> parameterNodes = new ArrayList<>(Arrays.asList(new VariableNode("number", true, Token.type.integer), new VariableNode("answer", false, Token.type.real)));

    public SquareRootNode() {
        super("squareRoot", parameterNodes, false);
    }

    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> params = new ArrayList<>();

        if(interpreterDataTypes.get(0) instanceof IntDataType) {
            IntDataType node = (IntDataType) interpreterDataTypes.get(0);
            params.add(new ParameterNode(new IntegerNode(node.value), false));
            FloatNode result = new FloatNode((float)Math.sqrt(node.value));
            params.add(new ParameterNode(result, true));
            return params;
        }
        throw new RuntimeException("Runtime Exception: Incompatable Types");
    }
}
