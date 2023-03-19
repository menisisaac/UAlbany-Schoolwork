import java.util.*;




public class Lexer {
    private List<Token> tokens = new ArrayList<>();
    private List<Token> allTokens = new ArrayList<>();
    private boolean isComment = false;
    private Set<Character> numberBegin = new HashSet<>(Arrays.asList('.', '+' , '-'));
    private HashMap<Character, Token.type> specialCharacters = new HashMap<>(){{
        put('+', Token.type.PLUS);
        put('-', Token.type.MINUS);
        put('*', Token.type.TIMES);
        put('/', Token.type.DIVIDE);
        put('>', Token.type.greaterThen);
    }};
    private HashMap<String, Token.type>  reservedWords = new HashMap(){{
        put("integer", Token.type.integer);
        put("real", Token.type.real);
        put("begin", Token.type.begin);
        put("end", Token.type.end);
        put("variables", Token.type.variables);
        put("constants", Token.type.constants);
        put("define", Token.type.define);
        put("var", Token.type.var);
        put("if", Token.type.ifControl);
        put("then", Token.type.thenControl);
        put("else", Token.type.elseControl);
        put("elsif", Token.type.elsifControl);
        put("for", Token.type.forLoop);
        put("from", Token.type.from);
        put("to", Token.type.to);
        put("while", Token.type.whileLoop);
        put("repeat", Token.type.repeatLoop);
        put("until", Token.type.until);
        put("mod", Token.type.mod);
        put("true", Token.type.trueBool);
        put("false", Token.type.falseBool);
        put("boolean", Token.type.booleanType);
        put("String", Token.type.Stringtype);
        put("char", Token.type.charType);
    }};
    private HashMap<Character, Token.type> specialSymbols = new HashMap(){{
        put(':', Token.type.colon);
        put(';', Token.type.semicolon);
        put('=', Token.type.equal);
        put(',', Token.type.comma);
        put('(', Token.type.leftParen);
        put(')', Token.type.rightParen);
        put('>', Token.type.greaterThen);
        put('<', Token.type.lessThan);
    }};


    /*
    Initializes Lexer
     */
    public Lexer() {}
    //Maps operator to token values


    //Removes whitespace to make line more readable for debugging
    public String format(String string) {
        String[] temp = string.split(" ");
        String result = "";
        for (String s : temp) {
            if(!s.equals("") && !s.equals(" ")) {
                result += s + " ";
            }
        }
        return result;
    }

    /*
    Number with decimal and number without decimal states
    Returns line position after number ends
     */
    public int numberState(String str, int position) {
        int two = position + 1;
        String result = Character.toString(str.charAt(position));
        boolean hasDecimal = false;
        if (result.equals(".")) {
            hasDecimal = true;
        }
        //Checks for number after
        if (result.equals("+") || result.equals("-")) {
            if(str.charAt(two) == ' ') {
                two++;
            }
        }
        while(two < str.length()) {
            if(Character.isDigit(str.charAt(two)) || str.charAt(two) == '.') {
                if (hasDecimal && str.charAt(two) == '.') {
                    throw new RuntimeException("Syntax Error: Double Decimal");
                }
                if (str.charAt(two) == '.') {
                    hasDecimal = true;
                }
                result += str.charAt(two);
                two++;
            } else {
                break;
            }
        }
        if (result.equals("-") || result.equals("+")) {
            throw new RuntimeException("Syntax Error: Not a number");
        }
        else {
            tokens.add(new Token(Token.type.NUMBER, result));
        }
        return two;
    }
    public int wordState(String str, int position) {
        int instancePosition = position;
        String result = "";
        while(instancePosition < str.length()) {
            if(Character.isAlphabetic(str.charAt(instancePosition))) {
                result += str.charAt(instancePosition);
                instancePosition++;
            } else {
                break;
            }
        }
        if(!result.equals("") && !result.equals(" ")) {
            if(reservedWords.containsKey(result)) {
                tokens.add(new Token(reservedWords.get(result)));
            } else {
                tokens.add(new Token(Token.type.Identifier, result));
            }
        }
        return instancePosition;
    }
    public int charState(String str, int position) {
        if(position + 1 < str.length()) {
            char result = str.charAt(position + 1);
            if(position + 2 < str.length()) {
                if(String.valueOf(str.charAt(position + 2)) == "'") {
                    tokens.add(new Token(Token.type.charType, String.valueOf(result)));
                    return position + 2;
                }
            }
        }
        throw new RuntimeException("Syntax Error: char not found");
    }

    /*
    State 0: First digit State
    State 1: Number state with or without decimal
    State 2: Operator State
    State 5: End of Line
    End of each state sends it into the next required state
     */
    public List<Token> lex(String line) {
        Stack<Character> stack = new Stack<Character>();
        tokens = new ArrayList<>();
        int state = 0;
        String str = format(line);
        int position = 0;
        while(position < str.length()) {
            if(isComment) {
                position = comment(str, position);
                continue;
            }
            if (str.charAt(position) == ' ' && position != str.length() - 1) {
                position++;
            } else if(str.charAt(position) == ' ' && position == str.length() - 1) {
                state = 5;
            }
            if(str.charAt(position) == '(') {
                if(position + 1 < str.length() && str.charAt(position + 1) == '*') {
                    isComment = true;
                    position = comment(str, position);
                    continue;
                } else {
                    //add to stack
                    stack.add('(');
                }
            }
            if(str.charAt(position) == ')') {
                if(stack.peek() == '(') {
                    stack.pop();
                }
            }
            if(str.charAt(position) == ':') {
                if(peek(str, position, '=')) {
                    tokens.add(new Token(Token.type.assignment));
                    position += 2;
                    continue;
                }
            }
            if(str.charAt(position) == '>') {
                if(peek(str, position, '=')) {
                    tokens.add(new Token(Token.type.greaterThanEqualTo));
                    position += 2;
                    state = 1;
                    continue;
                }
            }
            if(str.charAt(position) == '<') {
                if(peek(str, position, '=')) {
                    tokens.add(new Token(Token.type.lessThanEqualTo));
                    position += 2;
                    state = 1;
                    continue;
                }
            }
            if(str.charAt(position) == '<') {
                if(peek(str, position, '>')) {
                    tokens.add(new Token(Token.type.notEqual));
                    position += 2;
                    state = 1;
                    continue;
                }
            }

            if(specialSymbols.containsKey(str.charAt(position))) {
                tokens.add(new Token(specialSymbols.get(str.charAt(position))));
                state = 0;
                position++;
                continue;
            }
            switch (state) {
                case 0:
                    if (Character.isDigit(str.charAt(position)) || numberBegin.contains(str.charAt(position))) {
                        state = 1;
                    } else if(Character.isAlphabetic(str.charAt(position))) {
                        state = 3;
                    } else if(!tokens.isEmpty()) {
                        state = 2;
                    } else {
                        throw new RuntimeException("Invalid Syntax");
                    }
                    break;
                case 1:
                    if(Character.isAlphabetic(str.charAt(position))) {
                        state = 3;
                    } else {
                        position = numberState(str, position);
                        state = 2;
                    }
                    break;
                case 2:
                    if(Character.isAlphabetic(str.charAt(position))) {
                        state = 3;
                        continue;
                    }
                    if (specialCharacters.containsKey(str.charAt(position))) {
                        tokens.add(new Token(specialCharacters.get(str.charAt(position))));
                        position++;
                        state = 1;
                    } else {
                        throw new RuntimeException("Invalid Syntax: Expected Operator");
                    }
                    break;
                case 3:
                    position = wordState(str, position);
                    state = 0;
                    break;
                case 5:
                    position++;
                    break;
                default:
                    if (str.charAt(position) == ' ') {
                        position++;
                    }
                    throw new RuntimeException("Syntax Error");
            }
        }
        if(!stack.isEmpty() && isComment != true) {
            throw new RuntimeException("Syntax Error: Parenthesis do not close");
        } else if(isComment != true && !tokens.isEmpty()){
            tokens.add(new Token(Token.type.EndOfLine, null));
        }
        for(Token t : tokens) {
            allTokens.add(t);
        }
        return tokens;
    }

    public int comment(String str, int position) {
        int currPosition = position;
        while(true) {
            if(currPosition < str.length()) {
                if (str.charAt(currPosition) == '*') {
                    if (currPosition + 1 < str.length()) {
                        if (str.charAt(currPosition + 1) == ')') {
                            isComment = false;
                            return currPosition + 2;
                        } else {
                            currPosition++;
                        }
                    } else {
                        return str.length();
                    }
                } else {
                    currPosition++;
                }
            } else {
                return str.length();
            }
        }
    }

    public boolean peek(String str, int position, char c) {
        if(position + 1 < str.length()) {
            if(str.charAt(position + 1) == c) {
                return true;
            }
        }
        return false;
    }
    public List<Token> getAllTokens() {
        if(isComment) {
            throw new RuntimeException("Syntax Error: Comment never closes");
        }
        return allTokens;
    }

    public String toString() {
        String result = "";
        for(Token t : allTokens) {
            result += t + " ";
        }
        return result;
    }
}
