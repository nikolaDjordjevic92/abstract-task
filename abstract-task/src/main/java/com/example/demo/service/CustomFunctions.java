package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.security.web.util.matcher.IpAddressMatcher;

import com.creativewidgetworks.expressionparser.Function;
import com.creativewidgetworks.expressionparser.Parser;
import com.creativewidgetworks.expressionparser.Token;
import com.creativewidgetworks.expressionparser.Value;
import com.creativewidgetworks.expressionparser.ValueType;

public class CustomFunctions {
	private Parser parser;

	public static CustomFunctions register(Parser parser) {
		CustomFunctions toolbox = new CustomFunctions();
		toolbox.parser = parser;

		toolbox.parser
				.addFunction(new Function("ip_range", toolbox, "_ip_range", 2, 2, ValueType.STRING, ValueType.STRING));
		toolbox.parser.addFunction(new Function("in", toolbox, "_in", 2, 5, ValueType.STRING, ValueType.STRING,
				ValueType.STRING, ValueType.STRING, ValueType.STRING));

		return toolbox;
	}

	public Value _ip_range(Token function, Stack<Token> stack) {
		String ip_address_range = stack.pop().asString();
		String ip_address = stack.pop().asString();

		IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ip_address_range);

		return new Value(function.getText()).setValue(ipAddressMatcher.matches(ip_address));
	}

	public Value _in(Token function, Stack<Token> stack) {

		List<String> allowedRoles = new ArrayList<>();
		String userRole;
		switch (function.getArgc()) {
		case 5:
			allowedRoles.add(stack.pop().asString());
		case 4:
			allowedRoles.add(stack.pop().asString());
		case 3:
			allowedRoles.add(stack.pop().asString());
		case 2:
			allowedRoles.add(stack.pop().asString());
			userRole = stack.pop().asString();
			break;
		default:
			userRole = "";
			break;
		}

		boolean isAllowed = allowedRoles.stream().anyMatch(allowedRole -> allowedRole.equals(userRole));

		return new Value(function.getText()).setValue(isAllowed);
	}

}
