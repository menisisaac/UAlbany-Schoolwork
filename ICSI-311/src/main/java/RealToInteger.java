import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RealToInteger extends BuiltInFunctions{

    private static List<VariableNode> params = new ArrayList<>(Arrays.asList(new VariableNode("someReal", true, Token.type.real), new VariableNode("someInt", false, Token.type.integer)));
    public RealToInteger() {
        super("RealToInteger", params, false);
    }

    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> resultParams = new ArrayList<>();
        if(interpreterDataTypes.get(0) instanceof FloatDataType && interpreterDataTypes.get(1) instanceof IntDataType) {
            float temp = ((FloatDataType) interpreterDataTypes.get(0)).getValue();
            int result = (int)temp;
            resultParams.add(new ParameterNode(new FloatNode(temp), false));
            resultParams.add(new ParameterNode(new IntegerNode(result), true));
            return resultParams;
        }
        throw new RuntimeException("Runtime Error: Wrong data types");
    }
}
