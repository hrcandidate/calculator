package calculator;
import org.junit.*;
import  static org.junit.Assert.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
/**
 * Unit test for ExpressionTree
 */
public final class ExpressionTreeTest
{


  @BeforeClass
  public static void setupClass()  {
	BasicConfigurator.configure();

  }





  @Test
  public void expression_on_two_integers()
  {

    ExpressionTree calc = new ExpressionTree ("add(1,2)");
    assertTrue(calc.eval() == 3);
  }


  @Test
  public void ADD_with_single_nested_operater()
  {
    ExpressionTree calc = new ExpressionTree ("add(1, mult(2, 3))");
    assertTrue(calc.eval() == 7);
  }


  @Test
  public void ADD_with_two_nested_operators()
  {
    ExpressionTree calc = new ExpressionTree ("add(1, mult(2, 3))");
    assertTrue(calc.eval() == 7);
  }

  @Test
  public void MULT_with_two_nested_operators()
  {
    ExpressionTree calc = new ExpressionTree ("mult(add(2, 2), div(9, 3))");
    assertTrue(calc.eval() == 12);
  }


  @Test
  public void LET_with_single_expression()
  {
    ExpressionTree calc = new ExpressionTree ("let(a, 5, add(a, a))");
    assertTrue(calc.eval() == 10);
  }


  @Test
  public void LET_with_two_variables_and_two_nested_expressions()
  {
    ExpressionTree calc = new ExpressionTree ("let(a, 5, let(b, mult(a, 10), add(b, a)))");
    assertTrue(calc.eval() == 55);
  }




  @Test
  public void LET_with_complex_variable_assignment_expression()
  {
    ExpressionTree calc = new ExpressionTree ("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
    assertTrue(calc.eval() == 40);
  }



  @Test(expected=NumberFormatException.class)
  public void  expression_with_missing_bracket_should_throw_exception()
  {
    ExpressionTree calc = new ExpressionTree ("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))");
  }


  @Test(expected=NumberFormatException.class)
  public void  expression_with_invalid_operator_should_throw_exceptoin()
  {
    ExpressionTree calc = new ExpressionTree ("let(a, let(b, 10, add(b, b)), surprise(b, 20, add(a, b)))");
  }


  @Test(expected=IllegalArgumentException.class)
  public void  expression_with_integer_not_between_min_max_should_fail()
  {
    ExpressionTree calc = new ExpressionTree ("add(2147483647,1)");
    calc = new ExpressionTree ("add(-2147483647,1)");
  }


}
