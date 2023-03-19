import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
    A variadic function used to take in input from keyboard
    separated by spaces
 */
public class ReadNode extends BuiltInFunctions{
    public ReadNode() {
        super("read", null, true);
    }
    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> parameterNodes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] inputSplit = input.split(" ");
        int i = interpreterDataTypes.size();
        //Makes sure user inputs the correct number of variables
        if(i != inputSplit.length) {
            throw new RuntimeException("Runtime Error: Input Size does not match number of variables");
        }
        //Need to change to allow different data types
        for(int k = 0; k < inputSplit.length; k++) {
            if(interpreterDataTypes.get(k) instanceof FloatDataType) {
                FloatDataType temp = new FloatDataType();
                temp.FromString(inputSplit[k]);
                parameterNodes.add(new ParameterNode(new FloatNode(temp.getValue()), false));
            } else if(interpreterDataTypes.get(k) instanceof IntDataType) {
                IntDataType temp = new IntDataType();
                temp.FromString(inputSplit[k]);
                parameterNodes.add(new ParameterNode(new IntegerNode(temp.getValue()), false));
            } else if(interpreterDataTypes.get(k) instanceof CharDataType) {
                CharDataType temp = new CharDataType();
                temp.FromString(inputSplit[k]);
                parameterNodes.add(new ParameterNode(new CharNode(temp.toString()), false));
            } else if(interpreterDataTypes.get(k) instanceof  StringDataType) {
                StringDataType temp = new StringDataType();
                temp.FromString(inputSplit[k]);
                parameterNodes.add(new ParameterNode(new StringNode(temp.toString()), false));
            } else if(interpreterDataTypes.get(k) instanceof BooleanDataType) {
                BooleanDataType temp = new BooleanDataType();
                temp.FromString(inputSplit[k]);
                parameterNodes.add(new ParameterNode(new BoolNode(temp.isValue()), false));
            }
        }
        return parameterNodes;
    }
}
