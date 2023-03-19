import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Right extends BuiltInFunctions{
    private static List<VariableNode> params = new ArrayList<>(Arrays.asList(new VariableNode("someString", false, Token.type.Stringtype), new VariableNode("length", false, Token.type.integer)));
    public Right() {
        super("Right", params, false);
    }

    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> resultParams = new ArrayList<>();
        if(interpreterDataTypes.get(0) instanceof StringDataType && interpreterDataTypes.get(2) instanceof StringDataType
                && interpreterDataTypes.get(1) instanceof IntDataType) {
            String first = ((StringDataType) interpreterDataTypes.get(0)).getValue();
            int length = ((IntDataType) interpreterDataTypes.get(1)).getValue();
            String second = "";
            if(first.length() - length < 0) {
                throw new RuntimeException("Runtime Error: String index out of bounds");
            }
            for(int i = first.length() - length; i < first.length(); i++) {
                second += first.charAt(i);
            }
            resultParams.add(new ParameterNode(new StringNode(first), false));
            resultParams.add(new ParameterNode(new IntegerNode(length), false));
            resultParams.add(new ParameterNode(new StringNode(second), true));
            return resultParams;
        }
        throw new RuntimeException("Runtime Error: Wrong data type");
    }
}
