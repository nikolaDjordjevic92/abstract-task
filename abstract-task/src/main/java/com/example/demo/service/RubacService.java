package com.example.demo.service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Param;
import com.example.demo.model.Rule;

@Service
public class RubacService {
	
	public static final String FILE = "src/main/resources/workflows.json";

	public boolean checkRule(Param param) {

		JSONArray arrayOfRules = parseRuleFiles();

		for (Object rule : arrayOfRules) {
			JSONObject jsonObject = (JSONObject) rule;

			if (fitsTemplate(param.getRequest().getPath(), jsonObject.get("Path").toString())) {
				Map<String, String> mappedParams = parseParams(param, jsonObject);
				if (!parseAndValidateRules(jsonObject, mappedParams)) {
					return false;
				}
			}

		}

		return true;
	}

	private JSONArray parseRuleFiles() {
		JSONParser parser = new JSONParser();

		Object obj = null;
		try {
			obj = parser.parse(new FileReader(FILE));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return (JSONArray) obj;
	}

	private Map<String, String> parseParams(Param param, JSONObject jsonObject) {
		Map<String, String> mappedParams = new HashMap<>();
		JSONArray array = (JSONArray) jsonObject.get("Params");
		for (Object object : array) {
			JSONObject jsonParam = (JSONObject) object;
			mappedParams.put(jsonParam.get("Name").toString(),
					parseExpression(jsonParam.get("Expression").toString(), param));
		}

		return mappedParams;
	}

	private boolean parseAndValidateRules(JSONObject jsonObject, Map<String, String> mappedParams) {

		String paramExpresionPrefix = "";

		for (Map.Entry<String, String> set : mappedParams.entrySet()) {
			paramExpresionPrefix += "$" + set.getKey() + "='" + set.getValue() + "';";
		}

		JSONArray array = (JSONArray) jsonObject.get("Rules");
		for (Object object : array) {
			JSONObject jsonRule = (JSONObject) object;

			Rule rule = new Rule(jsonRule.get("RuleName").toString(), jsonRule.get("Expression").toString());
			if (!new ExpressionEvaluator().evaluateExpression(paramExpresionPrefix + rule.getExpression())) {
				return false;
			}
		}

		return true;
	}

	public String parseExpression(String unprocessedExpresion, Param param) {
		switch (unprocessedExpresion) {
		case "$request.getIpAddress": {
			return param.getRequest().getIp_address();
		}
		case "$user.getRole": {
			return param.getUser().getRole();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + unprocessedExpresion);
		}
	}

	private boolean fitsTemplate(String path, String template) {
		return FileSystems.getDefault().getPathMatcher("glob:" + template).matches(Paths.get(path));
	}
}
