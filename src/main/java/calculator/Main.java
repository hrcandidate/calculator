package calculator;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
/**
 * Created by hrcandidate on 09/11/16.
 */
public final class Main {

    public static final  String DEBUG="DEBUG";
    public static final  String INFO="INFO";
    public static final  String ERROR="ERROR";

    final static Logger LOG = Logger.getLogger(Main.class);

    private static String verbosity = System.getProperty("verbosity", ERROR);

    public  static void main (String ... args) {

        switch (verbosity) {
            case INFO:
                LOG.setLevel(Level.INFO);
		break;
            case DEBUG:
		LOG.setLevel(Level.DEBUG);
		break;
            case ERROR:
		LOG.setLevel(Level.ERROR);
		break;
            default:
		LOG.setLevel(Level.ERROR);
		break;

        }

        BasicConfigurator.configure();


        if (args.length < 1 || args.length > 1) {
	    LOG.error("did not provide an expression");
            throw new IllegalArgumentException("did not provide an expression");
        }


        ExpressionTree calc;
        calc = new ExpressionTree(args[0]);
	LOG.debug("Evaluating expression: "+args[0]);
        System.out.println(calc.eval());

    }
}
