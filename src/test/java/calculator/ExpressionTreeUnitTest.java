package calculator;
import org.junit.*;
import  static org.junit.Assert.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import static org.mockito.Mockito.*;
import static calculator.ExpressionTree.*;


public class ExpressionTreeUnitTest {

    ExpressionTree testET;
    ExpressionTree spyET;

    @Before
    public void setUp() {

        testET = new ExpressionTree("add(1,1)");
        spyET = spy(new ExpressionTree("add(1,1)"));

    }

    @Test
    public void parseExpression_givenLetExpression_should_call_getLetOperatorExpressions() {
       spyET.parseExpression("LET(a,5,add(a,a))", Operator.LET);
       verify(spyET,times(1)).getLetOperatorExpressions(anyString());
    }



    @Test
    public void parseExpression_givenArithmeticExpression_should_call_getArithmeticExpression() {
        spyET.parseExpression("add(1,5)", Operator.ADD);
        verify(spyET,times(1)).getArithmeticExpressions(anyString());
    }


    @Test
    public void parseExpression_givenArithmeticExpression_should_provide_operator_node() {
        assertTrue(spyET.parseExpression("add(1,5)", Operator.ADD).getType() == Type.OPERATOR);
    }


    @Test
    public void eval_givenAddExpression_should_succeed() {

        Node mockNode = getMockNode(Type.OPERATOR,Type.INTEGER,Operator.ADD,3,4);
        int i = spyET.eval(mockNode);

        assertEquals(7,i);
    }

    @Test
    public void eval_givenSubTractExpression_should_succeed() {

        Node mockNode = getMockNode(Type.OPERATOR,Type.INTEGER,Operator.SUB,3,4);
        int i = spyET.eval(mockNode);
        assertEquals(-1,i);
    }


    @Test
    public void eval_givenMultiExpression_should_succeed() {

        Node mockNode = getMockNode(Type.OPERATOR,Type.INTEGER,Operator.MULT,3,4);
        int i = spyET.eval(mockNode);
        assertEquals(12,i);
    }


    public void eval_givenDivideExpression_should_succeed() {

        Node mockNode = getMockNode(Type.OPERATOR,Type.INTEGER,Operator.DIV,3,4);
        int i = spyET.eval(mockNode);
        assertEquals(1,i);
    }


    @Test
    public void eval_givenLetExpression_should_do_nothing() {

        int NO_VALUE=0;
        Node mockNode = getMockNode(Type.OPERATOR,Type.INTEGER,Operator.LET,3,4);
        int i = spyET.eval(mockNode);
        assertEquals(NO_VALUE,i);
    }




    public Node getMockNode(Type type1,Type type2, Operator op, int val1, int val2) {
        Node mockNode = mock(Node.class);
        when(mockNode.getValue()).thenReturn(val1).thenReturn(val2);
        when(mockNode.getType()).thenReturn(type1).thenReturn(type2);
        when(mockNode.getOp()).thenReturn(op);
        when(mockNode.getRight()).thenReturn(mockNode);
        when(mockNode.getLeft()).thenReturn(mockNode);
        return mockNode;
    }


    @Test
    public void eval_checks_all_defined_operators_throw_no_exception() {

        int NO_VALUE=0;
        Node mockNode = null;
        for ( Operator op: Operator.values()) {
            mockNode = getMockNode(Type.OPERATOR, Type.INTEGER, op, 3, 4);
            int i = spyET.eval(mockNode);
        }

    }

    @Test
    public void getLetOperatorExpressions_should_provide_3_string_array()
    {
        String simpleLetExpression = "a,5,add(a,a))";
        String complextLetExpression ="a,LET(b,5,add(b,b)),add(a,a))";
        String[] simpleExpression = testET.getLetOperatorExpressions(simpleLetExpression);
        String[] complexExpression = testET.getLetOperatorExpressions(complextLetExpression);

        String[] expectedSimpleValues = new String[] {"a","5","add(a,a)"};
        String[] expectedComplexValues = new String[] {"a","LET(b,5,add(b,b))","add(a,a)"};

        assertArrayEquals(simpleLetExpression,expectedSimpleValues,simpleExpression);
        assertArrayEquals(complextLetExpression,expectedComplexValues,complexExpression);

    }


    @Test
    public void getArithmeticExpressions_add_should_provide_2_string_array()
    {
        String simpleArithmetic = "a,5)";
        String complextArithmetic ="b,add(1,2))";
        String[] simpleExpression = testET.getArithmeticExpressions(simpleArithmetic);
        String[] complexExpression = testET.getArithmeticExpressions(complextArithmetic);

        String[] expectedSimpleValues = new String[] {"a","5"};
        String[] expectedComplexValues = new String[] {"b","add(1,2)"};

        assertArrayEquals("decompose "+simpleArithmetic,expectedSimpleValues,simpleExpression);
        assertArrayEquals("decompose " +complextArithmetic,expectedComplexValues,complexExpression);

    }



}
