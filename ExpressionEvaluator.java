/**
 * The Class ExpressionEvaluator.
 */
// TODO: Auto-generated Javadoc
public class ExpressionEvaluator {
	// These are the required error strings for that MUST be returned on the appropriate error 
	/** The Constant PAREN_ERROR. */
	// for the JUnit tests to pass. DO NOT CHANGE!!!!!
	private static final String PAREN_ERROR = "Paren Error: ";
	
	/** The Constant OP_ERROR. */
	private static final String OP_ERROR = "Op Error: ";
	
	/** The Constant DATA_ERROR. */
	private static final String DATA_ERROR = "Data Error: ";
	
	/** The Constant DIV0_ERROR. */
	private static final String DIV0_ERROR = "Div0 Error: ";

	/** The data stack. */
	// The placeholder for the two stacks
	private GenericStack<Double> dataStack;
	
	/** The oper stack. */
	private GenericStack<String>  operStack;
	
	/** The is negative. */
	private boolean isNegative;
	
	/**
	 * Convert string to tokens.
	 *
	 * @param str the str
	 * @return the string[]
	 */
	private String[] convertStringToTokens(String str) {
		isNegative = false;
		str = incorrectFormat(str);
		str = insertSpaces(str);
		str = negativeNums(str);
		str = implicitMult(str);
		
		return str.split("\\s+");
	}
	
	/**
	 * Insert spaces / pad tokens.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String insertSpaces(String str) {
		str = str.replaceAll("(\\-|\\+|\\*|\\/|\\(|\\))", " $1 ");
		str = str.replaceFirst("\\s*", "");
		return str;
	}
	
	/**
	 * Negative nums. format negative nums.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String negativeNums(String str) {
		str = str.replaceAll("(\\-|\\+|\\*|\\/|\\() (\\-) (\\d+(\\.\\d+)?)", "$1 $2$3");
		str = str.replaceAll("^\\- (\\d+(\\.\\d+)?)", "-$1");
		return str;
	}
	
	/**
	 * Implicit mult.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String implicitMult(String str) {
		str = str.replaceAll("\\)\\s+([\\d\\.\\(])",") * $1");
		str = str.replaceAll("([\\d\\.])\\s+\\(","$1 * (");
		return str;
	}
	
	/**
	 * Incorrect negative format.
	 * 
	 * looks for incorrect formatting/simplifies formatting.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String incorrectFormat(String str) {
		if(str.contains("+-")) {
			int index = str.indexOf("+");
			str = str.substring(0, index) + "-" + str.substring(index + 2);
		} else if(str.contains("--")) {
			int index = str.indexOf("-");
			str = str.substring(0, index) + "+" + str.substring(index + 2);
		} else if(str.contains("*-")) {
			int index = str.indexOf("-");
			str = str.substring(0, index - 1) + "(" + str.substring(index + 1) + ")";
			isNegative = true;
		} else if(str.contains("/-")) {
			int index = str.indexOf("/");
			str = str.substring(0, index + 1) + str.substring(index + 2);
			isNegative = true;
		} else if(str.contains("+(-")) {
			int index = str.indexOf("+");
			str = str.substring(0, index) + str.substring(index + 2, str.length() - 1);
		} 
		if(str.contains("(-") && str.charAt(0) != '-') {
			int index2 = str.indexOf("(");
			str = str.substring(0, index2 - 2) + "-" + str.substring(index2 - 1, index2 + 1) + str.substring(index2 + 2);
		}
		if(str.contains("(-") && str.charAt(0) == '-') {
			int index = str.indexOf("(");
			str = str.substring(index - 1, index) + "(" +  str.substring(index + 2);
			isNegative = false;
		}
			return str;
	}
	
	/**
	 * Evaluate expression.
	 * 
	 * evaluates expressions after formatting input string.
	 *
	 * @param str the str
	 * @return the string
	 */
	protected String evaluateExpression(String str) {
		String saved = str;
		String eMessage = identifyError(str);
		
		if (!eMessage.equals("")) {
			return eMessage;
		}
        dataStack =  new GenericStack<Double>();
		operStack =  new GenericStack<String>();
		String[] tokens = convertStringToTokens(str);
		
		if(tokens.length == 2 && tokens[0].equals("-")) {
			return saved + "=" + saved;
		}
		
		try {
			for (String token : tokens) {
				if (isOperation(token) == false) {
					dataStack.push(Double.parseDouble(token));
				} else if (isOperation(token)) {
					if (operStack.empty() || token.equals("(")) {
						operStack.push(token);
						continue;
					} else if (isHigherPemdas(token, operStack.peek())) {
						operStack.push(token);
						continue;
					} else if (token.equals(")")) {
						while (!operStack.peek().equals("(")) {
							solve();
						}
							operStack.pop();
							continue;
					} else if(operStack.empty()) {
						dataStack.peek();
					} else {
						while(operStack.empty() == false && !operStack.peek().equals("(")
								&& isHigherPemdas(token, operStack.peek()) == false) {
							solve();
						}
							operStack.push(token);
					}
				}
			}
				while (operStack.empty() == false) {
					solve();
				}
				
			} catch (ArithmeticException e) {
				return DIV0_ERROR;
			}

				if(isNegative == true) {
					return (saved + "=" + "-" + dataStack.peek());
				}
				return (saved + "=" + dataStack.peek());
	}
	
	/**
	 * Checks if is operation.
	 * 
	 * determines if operation is valid.
	 *
	 * @param token the token
	 * @return true, if is operation
	 */
	private boolean isOperation(String token) {
		if(token.equals("(") || token.equals(")") || token.equals("+") || token.equals("-")
			|| token.equals("*") || token.equals("/")) {
			return true;
		}
			return false;
	}
	
	/**
	 * Checks if is higher pemdas.
	 * 
	 * compares operators.
	 *
	 * @param token the token
	 * @param stack the stack
	 * @return true, if is higher pemdas
	 */
	private boolean isHigherPemdas(String token, String stack) {
		if((token.equals("*") || token.equals("/")) && (stack.equals("+") || stack.equals("-"))) {
			return true;
		}
			return false;
	}
	
	/**
	 * Solve. solves arithmetic operations based on comparison in isHigherPemdas in evaluate.
	 */
	private void solve() {
		double d2 = dataStack.pop();
		double d1 = dataStack.pop();
		String op = operStack.pop();
		
		if(op.equals("+")) {
			dataStack.push(d1 + d2);
		} else if(op.equals("-")) {
			dataStack.push(d1 - d2);
		} else if(op.equals("*")) {
			dataStack.push(d1 * d2);
		} else if(op.equals("/")) {
			if(d2 == 0) {
				throw new ArithmeticException("can't divide by 0!");
			} else {
				dataStack.push(d1 / d2);
			}
		}
	}
	
	/**
	 * Identify type of error.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String identifyError(String str) {
		if (BracketError(str) == false) {
			return PAREN_ERROR;
		} else if (DataStackError(str) == false) {
			return DATA_ERROR;
		} else if (OperationStackError(str) == false) {
			return OP_ERROR;
		} else {	
			return "";
		}
	}
	
	/**
	 * Match paren.
	 *
	 * @param in the in
	 * @return true, if successful
	 */
	private boolean matchParen(String in) {
		int depth = 0;
		int index = 0;
		
		for(;index < in.length(); index++) {
			char c = in.charAt(index);
			
			if (c == '(') {
				depth++;
			} else if(c == ')') {
				depth--;
			}
			
			if (depth < 0) {
				return false;
			}
		}
		return depth == 0;
	}
	
	/**
	 * Bracket error.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	private boolean BracketError(String str) {
		if (matchParen(str) == false) {
			return false;
		} else if (str.matches("\\(\\s*\\)")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Data stack error.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	private boolean DataStackError(String str) {
		if (str.matches("(\\d|\\-|\\+|\\*|\\/|\\(|\\)|\\.|\\s)+") == false) {
			return false;
		} else if (str.matches(".*\\d+(\\.\\d+)?\\s+\\d+(\\.\\d+)?.*")) {
			return false;
		} else if(str.contains("..")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Operation stack error.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	private boolean OperationStackError(String str) {
		String reg = "(\\+|\\*|\\/)";
		String regTwo = "(\\+|\\*|\\/|\\-)";
		
		if (str.matches(".*" + reg + "\\s*" + reg + ".*")) {
			return false;
		} else if (str.trim().matches(".*" + regTwo + "$") || str.trim().matches("^" + reg + ".*")) {
			return false;
		} else if (str.matches(".*\\-\\s*\\-\\s*" + regTwo + ".*") || 
				str.matches(".*(\\-|\\+|\\*|\\/)(\\))(\\-|\\+|\\*|\\/).*")) {
			return false;
		} else if(str.contains("(+") || str.contains("* -")) {
			return false;
		}
			return true;
	}

}
