package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by hrcandidate on 09/11/16.
 */
public class ExpressionTree {


  //final static Logger LOG = Logger.getLogger(ExpressionTree.class);

    public enum Operator {
        ADD, DIV, MULT, SUB, LET,
    }

    public enum Type {

        VARIABLE, INTEGER, OPERATOR
    }


    /* static modifier meant to help avoid inadvertent access to ExpressionTree info */
    public static class Node {

        private Node left, right;  //subtrees
        private Operator op; //set on only operators
        private Type type; //set on all nodes
        private int value; //only if an integer
        private String var; //only if a variable

        public Node(Type type, Operator op, int value, String var) {

            this.type = type;
            this.op = op;
            this.value = value;
            this.var = var;
        }

    }

        private Node root = null;
        private Map<String, Integer> variables = new HashMap<String, Integer>();



    /* when evaluating  nested LET operations using an ExpressTree
       this constructor helps pass previously
       found variables  that may be used in the expression*/

    public ExpressionTree(String expr, Map<String,Integer> variables) {
            this.variables = variables;
            // removing spaces and uniforming case from express
            this.root = parseExpression(expr.toUpperCase().replace(" ",""));

        }

        public ExpressionTree(String expr) {
            this(expr,new HashMap<String, Integer>());

        }


    /**
     * Method to retrieve the three arguments in a LET operation
     *  e.g.  LET ( a,5,add(a,a))
     *        extracts variable name "A", value "5", expression add(a,a)
     *        as strings
     * @return String[] with the three arguments
     */

        public String[] getThreeNestedExpressions(String expr) {
            Stack<String> stack = new Stack<String>(); //will be used for fancy stuff later
            char[] exprArray = expr.toCharArray();
            String[] result = new String[3];
            result[0] = expr.substring(0, 1);  //remove the 1 character variable

            for (int i = 2; i < exprArray.length; i++) {
                if ('(' == exprArray[i] && i != 0)
                    stack.push("(");
                else if (')' == exprArray[i])
                    stack.pop();
                else if (',' == exprArray[i] && stack.isEmpty()) {
                    result[1] = expr.substring(2, i);
                    result[2] = expr.substring(i + 1, expr.length() - 1);
                    break;
                }

            }

            return result;


        }

    /**
     * Method to retrieve the two arguments in an arithmetic function
     *
     * @return String[] with two arguments
     */
        public String[] getTwoNestedExpressions(String expr) {

            Stack<String> stack = new Stack<String>();
            char[] exprArray = expr.toCharArray();
            String[] result = new String[2];

            /* goes through each chracter in expression escaping
               nested parentheses to find operands.
             */
            for (int i = 0; i < exprArray.length; i++) {
                if ('(' == exprArray[i] && i != 0) {
                    stack.push("(");
                } else if (')' == exprArray[i])
                    stack.pop();
                else if (',' == exprArray[i] && stack.isEmpty()) {
                    result[0] = expr.substring(0, i);
                    result[1] = expr.substring(i + 1, expr.length() - 1);
                    break;
                }

            }

            return result;


        }



    /**
     * parses an expression by recursing over each operator or returning variable
     * values or integer values.
     * TODO: collapse repetitive if statements to enum iterator
     * @return Node representing expression
     */
        public Node parseExpression(String expr) {
            Node node = null;
            Operator operator = null;


            validateString(expr);


            for (Operator op : Operator.values())
                if (expr.startsWith(op.name()))
                    operator = op;

            //matches an operator
            if (operator != null)
                node = parseExpression(expr, operator);
            //matches a variable
            else if (expr.matches("[A-z]"))
                node = new Node(Type.INTEGER, null, variables.get(expr), null);
            //matches an Integer
            else {
                try {
                    node = new Node(Type.INTEGER, null, Integer.valueOf(expr), null);
                } catch (NumberFormatException e) {
                    System.out.println("invalid expression probably missing parenthesis or comma, invalid expression="+expr);
                    throw e;
                }
            }


            return node;
        }

    private void validateString(String expr) {
        if (expr == null)
            throw new IllegalArgumentException("expression provided incorrect");
    }


    /**
     * parses an expression recursively for operations and handles the special condition
     * for the LET operator assigning variables.
     * @return Node represening expression
     */
        public Node parseExpression(String expr, Operator op) {

            Node node;
            int skip = (op == Operator.MULT) ? 5 : 4;

            if (op == Operator.LET) { //handles special LET node
                String[] threes = getThreeNestedExpressions(expr.substring(skip));
                variables.put(threes[0], new ExpressionTree(threes[1],variables).eval());
                node = parseExpression(threes[2]);

            } else {
                node = new Node(Type.OPERATOR, op, 0, null);
                String[] twos = getTwoNestedExpressions(expr.substring(skip));
                node.left = parseExpression(twos[0]);
                node.right = parseExpression(twos[1]);
            }
            return node;
        }



    /**
     * Evaluate full ExpressionTree by going through nodes post order starting at root

     * @return Node represening expression
     */
        public int eval() {
            //when evaluating start at root prefix operator
            return eval(root);
        }





    /**
     * evaluate expression on each Node going post order until no nodes left
     * TODO: can math operations be found in enum instead to avoid switch statement
     *  e.g.  LET ( a,5,add(a,a))
     *        extracts variable name "A", value "5", expression add(a,a)
     *        as strings
     * @return Node represening expression
     * @throw Illegal argument if an Operator that exists in the enum isn't interpreted
     */
        public int eval(Node node) {
            int result = 0;


            if (node.type == Type.INTEGER)
                result = node.value;
            else if (node.type == Type.VARIABLE)
                result = variables.get(node.var);
            else { //do math first

                    int left = eval(node.left);
                    int right = eval(node.right);

                    switch (node.op) {
                        case ADD:
                            result = left + right;
                            break;
                        case MULT:
                            result = left * right;
                            break;
                        case DIV:
                            result = left / right;
                            break;
                        case SUB:
                            result = left - right;
                            break;
                        case LET:
                            //do nothing
                            break;
                        default:
                            throw new
                                    IllegalArgumentException("use arithmetic operator that is not ADD,MULT,DIV,SUB,LET");
                    }
                }

            return result;
        }

    }

