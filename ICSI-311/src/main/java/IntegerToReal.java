import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegerToReal extends BuiltInFunctions{
    private static List<VariableNode> params = new ArrayList<>(Arrays.asList(new VariableNode("someInt", true, Token.type.integer), new VariableNode("someReal", false, Token.type.real)));
    public IntegerToReal() {
        super("IntegerToReal", params, false);
    }

    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> resultParams = new ArrayList<>();
        if(interpreterDataTypes.get(0) instanceof IntDataType && interpreterDataTypes.get(1) instanceof FloatDataType) {
            int temp = ((IntDataType) interpreterDataTypes.get(0)).getValue();
            float result = (float)temp;
            resultParams.add(new ParameterNode(new IntegerNode(temp), false));
            resultParams.add(new ParameterNode(new FloatNode(result), true));
            return resultParams;
        }
        throw new RuntimeException("Runtime Error: Wrong data types");
    }
}
