package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import structures.Stack;

public class Expression {	
	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr
	 *            The expression
	 * @param vars
	 *            The variables array list - already created by the caller
	 * @param arrays
	 *            The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/
		int j = 0;
		for (int i = 0; i < expr.length(); i++){
			if (Character.isLetter(expr.charAt(i))) {
				String extra = "";
				boolean isArray = false;
				j=i;
				while (j < expr.length() && Character.isLetter(expr.charAt(j))){
					extra += expr.charAt(j);
					j++;
				}
				i=j;
				if (i < expr.length() && expr.charAt(i) == '[')
					isArray = true;

				if (isArray) {
					Array a = new Array(extra);
					arrays.add(a);
				} else {
					Variable b = new Variable(extra);
					vars.add(b);
				}
			}
		}
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc
	 *            Scanner for values input
	 * @throws IOException
	 *             If there is a problem with the input
	 * @param vars
	 *            The variables array list, previously populated by
	 *            makeVariableLists
	 * @param arrays
	 *            The arrays array list - previously populated by makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars
	 *            The variables array list, with values for all variables in the
	 *            expression
	 * @param arrays
	 *            The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float 
	evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation
    		Stack<Float> stackFloat = new Stack<Float>();
    		Stack<Character> stackSymbol = new Stack<Character>();
    		Stack<Float> stackFloat2 = new Stack<Float>();
    		Stack<Character> stackSymbol2 = new Stack<Character>();
    		String temp;
    		int counter = 1;
    		float sum = 0;
    		float num = 0;
    		float num1;
    		float num2;
    		int rec;
    		float rec2;
    		int i = 0;
    		int j = 0;
    		char a;
    		String var = "";
    		while (i != expr.length()){
    			a = expr.charAt(i);
    			if (a == ' '){
    				i = i+1;
    				if (i+1 == expr.length() && var != ""){
    					if (Character.isLetter(var.charAt(0))){
    						while (j != vars.size()){
    							if (vars.get(j).name.equals(var)){
    								num = vars.get(j).value;
    								j=0;
    								break;
    							}
    							j++;
    						}
    					} else {
    						num = Float.parseFloat(var);
    					}
    					stackFloat.push(num);
    				}
    				continue;
    			} else if (a == ']' || a == ')'){
    				if (var != ""){
    					if (Character.isLetter(var.charAt(0))){
    						while (j != vars.size()){
    							if (vars.get(j).name.equals(var)){
    								num = vars.get(j).value;
    								j=0;
    								break;
    							}
    							j++;
    						}
    					} else {
    						num = Float.parseFloat(var);
    					}
    				}
    				stackFloat.push(num);
    				break;
    			} else if (i + 1 == expr.length() && (Character.isDigit(a) || Character.isLetter(a))){
					var = var + a;
					if (Character.isLetter(a)){
						while (j != vars.size()){
							if (vars.get(j).name.equals(var)){
								num = vars.get(j).value;
								j=0;
								break;
							}
							j++;
						}
					} else {
						num = Float.parseFloat(var);
					}
					stackFloat.push(num);
			} else if (Character.isDigit(a) || Character.isLetter(a)){
    				var = var + a;
    			} else if (a == '+' || a == '-' || a == '*' || a == '/'){
    				if (Character.isLetter(var.charAt(0))){
    						while (j != vars.size()){
    							if (vars.get(j).name.equals(var)){
    								num = vars.get(j).value;
    								j=0;
    								break;
    							}
    							j++;
    						}
    				} else {
    					num = Float.parseFloat(var);
    				}
    				stackFloat.push(num);
    				if (i+1 != expr.length()){
    					stackSymbol.push(a);
    				}
    				var = "";
    			} else if (a == '['){
    				while (j != arrays.size()){
						if (arrays.get(j).name.equals(var)){
							temp = expr;
							expr = expr.substring(i+1);
							rec = (int) evaluate(expr, vars, arrays); 
							expr = temp;
							var = "" + arrays.get(j).values[rec];
							i++;
							while (counter != 0){
								if(expr.charAt(i) == '['){
									counter++;
								} else if (expr.charAt(i) == ']'){	
								    counter--;
								}
								i++;
		    				}
		    				i--;
		    				j = 0;
		    				counter = 1;
							if(i+1 == expr.length()){
								if (Character.isLetter(a)){
		    						while (j != vars.size()){
		    							if (vars.get(j).name.equals(var)){
		    								num = vars.get(j).value;
		    								j=0;
		    								break;
		    							}
		    							j++;
		    						}
		    						stackFloat.push(num);
		    					} else {
		    						num = Float.parseFloat(var);
		    						stackFloat.push(num);
		    					}
							}
							break;
						}
						j++;
    				}
    			} else if (a == '('){
    				temp = expr;
					expr = expr.substring(i+1);
					rec2 = evaluate(expr, vars, arrays);
					expr = temp;
    				var = "" + rec2;
    				i++;
    				while (counter != 0){
						if(expr.charAt(i) == '('){
							counter++;
						} else if (expr.charAt(i) == ')'){	
						    counter--;
						}
						i++;
    				}
    				i--;
    				j = 0;
    				counter = 1;
    				if(i+1 == expr.length()){
    					num = Float.parseFloat(var);
    					stackFloat.push(num);
    				}
    			}
    			i++;
    		}
    		while(!stackFloat.isEmpty()){
    			stackFloat2.push(stackFloat.pop());
    		}
    		while(!stackSymbol.isEmpty()){
    			stackSymbol2.push(stackSymbol.pop());
    		}
    		while (stackFloat2.size() > 1){
    			if (stackSymbol2.peek() == '*'){
    				num1 = stackFloat2.pop();
        			num2 = stackFloat2.pop();
        			stackFloat2.push(num1*num2);
        			stackSymbol2.pop();
    			} else if (stackSymbol2.peek() == '/'){
        			num1 = stackFloat2.pop();
                	num2 = stackFloat2.pop();
                	stackFloat2.push(num1/num2);
                	stackSymbol2.pop();
    			} else if (stackSymbol2.peek() == '+'){
    				sum+=stackFloat2.pop();
    				stackSymbol2.pop();
    			} else if (stackSymbol2.peek() == '-'){
    				sum+=stackFloat2.pop();
    				num1 = -stackFloat2.pop();
    				stackFloat2.push(num1);
    				stackSymbol2.pop();
    			} 
    		}
    		sum+=stackFloat2.pop();
    		return sum;
	}
}