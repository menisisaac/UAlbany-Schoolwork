public class Token {

    private String value;
    private type instanceType;
    //Types with values constructor
    public Token(type instanceType, String value) {
        this.value = value;
        this.instanceType = instanceType;
    }
    //Operator Constructor
    public Token(type instanceType) {
        this.instanceType = instanceType;
        this.value = null;
    }

    public String getValue() {
        return value;
    }

    public type getInstanceType() {
        return instanceType;
    }

    public enum type{
        EndOfLine, NUMBER, MINUS, PLUS, TIMES, DIVIDE, leftParen, rightParen,
        Identifier, define, integer, real, begin, end, semicolon, colon, equal,
        comma, variables, constants, var, assignment, greaterThen, lessThan,
        greaterThanEqualTo, lessThanEqualTo, notEqual, ifControl, thenControl,
        elseControl, elsifControl, forLoop, from, to, whileLoop, repeatLoop,
        until, mod, trueBool, falseBool, booleanType, charType, Stringtype
    }
    //Displays value only for Numbers
    public String toString() {
        if(getValue() != null) {
            return instanceType + "(" + value + ")";
        } else {
            return instanceType + "";
        }

    }
}
