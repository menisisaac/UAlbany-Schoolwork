import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GetRandom extends BuiltInFunctions{
    private static List<VariableNode> params = new ArrayList<>(Arrays.asList(new VariableNode("resultInteger", false, Token.type.integer)));
    public GetRandom() {
        super("GetRandom", params, false);
    }

    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> resultParams = new ArrayList<>();
        Random random = new Random();
        resultParams.add(new ParameterNode(new IntegerNode(random.nextInt()), true));
        return resultParams;
    }
}
