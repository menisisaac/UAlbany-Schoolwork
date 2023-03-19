import java.util.List;
/*
    Structure on which shank specific functions are built on
 */
public abstract class BuiltInFunctions extends CallableNode{
    private boolean isVariadic;
    public BuiltInFunctions(String name, List<VariableNode> parameters, boolean isVariadic) {
        super(name, parameters);
        this.isVariadic = isVariadic;
    }
    public boolean isVariadic() {
        return isVariadic;
    }
    public abstract List<ParameterNode> Execute(List<InterpreterDataType> interpreterDataTypes);
}
