package com.example.demo.service;

import java.util.List;

import com.creativewidgetworks.expressionparser.FunctionToolbox;
import com.creativewidgetworks.expressionparser.Parser;
import com.creativewidgetworks.expressionparser.ParserException;
import com.creativewidgetworks.expressionparser.Token;
import com.creativewidgetworks.expressionparser.Value;

public class ExpressionEvaluator {
	
	
	public Boolean evaluateExpression(String expression) {
        DemoParser parser = new DemoParser();

        FunctionToolbox.register(parser);
        
        CustomFunctions.register(parser);
        Value value = parser.eval(expression);

        if (value != null) {
        	return value.asBoolean();
        }
        
        return false;
    }

	class DemoParser extends Parser {
	    private List<Token> infix;
	    private List<Token> rpn;
	
	    DemoParser() {
	        super();
	    }
	
	    List<Token> getInfix() {
	        return infix;
	    }
	
	    List<Token> getPostfix() {
	        return rpn;
	    }
	
	    @Override
	    public List<Token> infixToRPN(List<Token> inputTokens) throws ParserException {
	        infix = inputTokens;
	        rpn = super.infixToRPN(inputTokens);
	        return rpn;
    }
}

}
