package nl.han.ica.icss.utils.comparator.calculator;

import com.google.errorprone.annotations.Var;
import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.VariablesHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calculator {
    private final Stack<String> shuntingYardOperators = new Stack<>();
    private final Queue<Object> shuntingYardOutput = new LinkedList<>();
    private final Stack<Integer> operands = new Stack<>();
    private VariablesHandler handler;
    private HashMap<Integer, ExpressionType> evaluationResults = new HashMap<>();

    int precedence = 0;

    public void setTokens(ASTNode node, VariablesHandler handler) {
        this.handler = handler;
        arrangeTokens(node);
    }

    private void arrangeTokens(ASTNode node) {
        if (node instanceof Operation) {
            addOperandsToShuntingYardOutput(node);
            addOperatorsToShuntingYardOperators(node);
            precedence = getPrecedence(node.getNodeLabel());
            if (node.getChildren().get(1).getChildren().size() == 0) {
                mergeDatastructures();
            }
        }
    }

    private void addOperandsToShuntingYardOutput(ASTNode node) {
        addVariableValuesToOutput(node);
        if (node.getChildren().get(0) instanceof Literal) {
            shuntingYardOutput.add(getValueOf(node.getChildren().get(0)));
            if (node.getChildren().get(1) instanceof Operation) {
                shuntingYardOutput.add(getValueOf(node.getChildren().get(1)));
            }
        }
    }

    private void addVariableValuesToOutput(ASTNode operation) {
        for (ASTNode node : operation.getChildren()) {
            if (node instanceof VariableReference) {
                shuntingYardOutput.add(handler.getValueFrom(node));
            }
        }
    }

    private void addOperatorsToShuntingYardOperators(ASTNode node) {
        if (shuntingYardOperators.empty()) {
            shuntingYardOperators.add(node.getNodeLabel());
        } else if (getPrecedence(node.getNodeLabel()) < precedence) {
            int lastNum = (int) shuntingYardOutput.remove();
            shuntingYardOutput.add(shuntingYardOperators.pop());
            shuntingYardOutput.add(lastNum);
            shuntingYardOperators.add(node.getNodeLabel());
        } else {
            shuntingYardOperators.add(node.getNodeLabel());
        }
    }

    private void mergeDatastructures() {
        while (!shuntingYardOperators.empty()) {
            shuntingYardOutput.add(shuntingYardOperators.pop());
        }
    }

    private int getPrecedence(String nodeLabel) {
        return nodeLabel.equals("Add") || nodeLabel.equals("Subtract") ? 1 : 2;
    }

    private int getValueOf(ASTNode literal) {
        int value = 0;

        if (literal instanceof PixelLiteral) {
            PixelLiteral number = (PixelLiteral) literal;
            value = (int) number.value;
        } else if (literal instanceof PercentageLiteral) {
            PercentageLiteral number = (PercentageLiteral) literal;
            value = (int) number.value;
        } else if (literal instanceof ScalarLiteral) {
            ScalarLiteral number = (ScalarLiteral) literal;
            value = (int) number.value;
        }
        return value;
    }

    public int calculate() {
        int sum = 0;
        for (Object token : shuntingYardOutput) {
            if (token instanceof Integer) {
                operands.add((int) token);
            } else {
                if (token.equals("Multiply")) {
                    int num1 = operands.pop();
                    int num2 = operands.pop();
                    sum = num1 * num2;
                    operands.add(sum);
                } else if (token.equals("Add")) {
                    int num1 = operands.pop();
                    int num2 = operands.pop();
                    sum = num1 + num2;
                    operands.add(sum);
                }  else if (token.equals("Subtract")) {
                    int num1 = operands.pop();
                    int num2 = operands.pop();
                    sum = num2 - num1;
                    operands.add(sum);
                }
            }
        }
        return sum;
    }
}
