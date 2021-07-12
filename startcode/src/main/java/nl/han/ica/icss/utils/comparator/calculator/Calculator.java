package nl.han.ica.icss.utils.comparator.calculator;

import com.google.errorprone.annotations.Var;
import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.VariablesHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calculator {
    private Stack<String> operators = new Stack<>();
    private Queue<Object> output = new LinkedList<>();
    private Stack<Integer> operands = new Stack<>();
    private VariablesHandler handler = new VariablesHandler();
    private HANLinkedList<HashMap<String, Object>> variableValues;

    int precedence = 0;

    public void setTokens(ASTNode node, HANLinkedList<HashMap<String, Object>> variableValues) {
        this.variableValues = variableValues;
        arrangeTokens(node);
    }

    private void arrangeTokens(ASTNode node) {
        if (node instanceof Operation) {
            setOperands(node);
            setOperators(node);
            precedence = getPrecedence(node.getNodeLabel());
            if (node.getChildren().get(1).getChildren().size() == 0) {
                mergeDatastructures();
            }
        }
    }

    private void setOperands(ASTNode node) {
        checkForVariableReferences(node);
        if (node.getChildren().get(0) instanceof Literal) {
            output.add(getValue(node.getChildren().get(0)));
            if (!(node.getChildren().get(1) instanceof Operation)) {
                output.add(getValue(node.getChildren().get(1)));
            }
        }
    }

    private void checkForVariableReferences(ASTNode operation) {
        for (ASTNode node : operation.getChildren()) {
            if (!(node instanceof VariableReference)) {
                return;
            }
//            output.add(handler.getValue(node));
        }
    }

    private void setOperators(ASTNode node) {
        if (operators.empty()) {
            operators.add(node.getNodeLabel());
        } else if (getPrecedence(node.getNodeLabel()) < precedence) {
            int lastNum = (int) output.remove();
            output.add(operators.pop());
            output.add(lastNum);
            operators.add(node.getNodeLabel());
        } else {
            operators.add(node.getNodeLabel());
        }
    }

    private void mergeDatastructures() {
        while (!operators.empty()) {
            output.add(operators.pop());
        }
    }

    private int getPrecedence(String nodeLabel) {
        return nodeLabel.equals("Add") || nodeLabel.equals("Subtract") ? 1 : 2;
    }

    private int getValue(ASTNode literal) {
        int value = 0;

//        if (literal instanceof PixelLiteral) {
//            PixelLiteral number = (PixelLiteral) literal;
//            value = number.value;
//        } else if (literal instanceof PercentageLiteral) {
//            PercentageLiteral number = (PercentageLiteral) literal;
//            value = number.value;
//        } else if (literal instanceof ScalarLiteral) {
//            ScalarLiteral number = (ScalarLiteral) literal;
//            value = number.value;
//        }
        return value;
    }

    public int calculate() {
        int sum = 0;
        for (Object token : output) {
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
