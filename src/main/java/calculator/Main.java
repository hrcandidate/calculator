package calculator;


/**
 * Created by hrcandidate on 09/11/16.
 */
public final class Main {

    public static final  String DEBUG="DEBUG";
    public static final  String INFO="INFO";
    public static final  String ERROR="ERROR";

    //final static Logger LOG = Logger.getLogger(Main.class);

    private static String verbosity = System.getProperty("verbosity", INFO);

    public  static void main (String ... args) {


        if (args.length < 1 || args.length > 1) {
            throw new IllegalArgumentException("did not provide an expression");
        }

        switch (verbosity) {
            case INFO:
            case DEBUG:
            case ERROR:
            default:

        }

        ExpressionTree calc;
        calc = new ExpressionTree(args[0]);
        System.out.println(calc.eval());

    }
}
