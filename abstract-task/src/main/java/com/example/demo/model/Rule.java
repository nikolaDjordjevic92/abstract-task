package com.example.demo.model;

public class Rule {

	private String ruleName;
	private String expression;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Rule() {
	}

	public Rule(String ruleName, String expression) {
		super();
		this.ruleName = ruleName;
		this.expression = expression;
	}

}
