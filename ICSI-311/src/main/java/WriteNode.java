import java.util.ArrayList;
import java.util.List;
/*
    A variadic function used to write output to the screen from parameters
 */
public class WriteNode extends BuiltInFunctions{
    public WriteNode() {
        super("write", null, true);
    }
    @Override
    public List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes) {
        List<ParameterNode> parameterNodes = new ArrayList<>();
        for(InterpreterDataType interpreterDataType : interpreterDataTypes) {
            System.out.print(interpreterDataType);
        }
        System.out.println("");
        for(int i = 0; i < interpreterDataTypes.size(); i++) {
            if(interpreterDataTypes.get(i) instanceof FloatDataType) {
                parameterNodes.add(new ParameterNode(new FloatNode(((FloatDataType) interpreterDataTypes.get(i)).getValue()), false));
            } else if(interpreterDataTypes.get(i) instanceof IntDataType) {
                parameterNodes.add(new ParameterNode(new IntegerNode(((IntDataType) interpreterDataTypes.get(i)).getValue()), false));
            } else if(interpreterDataTypes.get(i) instanceof CharDataType) {
                parameterNodes.add(new ParameterNode(new CharNode((String.valueOf(((CharDataType)(interpreterDataTypes.get(i))).getValue()))), false));
            } else if(interpreterDataTypes.get(i) instanceof  StringDataType) {
                parameterNodes.add(new ParameterNode(new StringNode(((StringDataType) interpreterDataTypes.get(i)).getValue()), false));
            } else if(interpreterDataTypes.get(i) instanceof BooleanDataType) {
                parameterNodes.add(new ParameterNode(new BoolNode(((BooleanDataType)interpreterDataTypes.get(i)).isValue()), false));
            }
        }
        return parameterNodes;
    }
}
