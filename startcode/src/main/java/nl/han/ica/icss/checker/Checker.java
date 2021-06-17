package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;

public class Checker {
    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private HANLinkedList<HashMap<String, String>> variableValues;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        variableValues = new HANLinkedList<>();
        validateNodes(ast.root.getChildren());
    }

    private void validateNodes(ArrayList<ASTNode> nodes) {
        for(ASTNode node : nodes) {
            getUndefinedVariables(node);
            getUnApprovedOperations(node);

            validateNodes(node.getChildren());
        }
    }

    private void getUnApprovedOperations(ASTNode node) {
        getUnApprovedAddOrSubtractOperation(node);
        getUnApprovedMultiplyOperation(node);
    }

    private void isColorLiteral(ASTNode operandLeft, ASTNode operandRight) {
        for (int i = 0; i < variableTypes.getSize(); i++) {
            String variableKey1 = operandLeft.toString().substring(20, operandLeft.getNodeLabel().length());
//            String variableKey2 = operandRight.toString().substring(20, operandRight.getNodeLabel().length());
            if (variableTypes.get(i).containsKey(variableKey1)) {
                ExpressionType literal = variableTypes.get(i).get(variableKey1);
                if (literal.equals(ExpressionType.COLOR)) {
                    operandLeft.setError("Is van het type COLOR");
                }
            }
        }
    }

    private void getUnApprovedAddOrSubtractOperation(ASTNode node) {
        if (node instanceof AddOperation || node instanceof SubtractOperation) {
            ASTNode operandLeft = node.getChildren().get(0);
            ASTNode operandRight = node.getChildren().get(1);
            isColorLiteral(operandLeft, operandRight);
            if (operandRight instanceof AddOperation || operandRight instanceof SubtractOperation) {
                String typeLeft = operandLeft.getClass().getSimpleName();
                String typeLeftChild = operandRight.getChildren().get(0).getClass().getSimpleName();
                if (!typeLeft.equals(typeLeftChild)) {
                    node.setError("Iets om later te wijzigen LINKERKIND NIET GELIJK AAN LINKERPARENT");
                }
            }
            else if (operandRight instanceof MultiplyOperation) {
                getUnApprovedMultiplyOperation(node);
            }
            else if (!compareNonOperationalOperands(operandLeft.getClass().getSimpleName(), operandRight.getClass().getSimpleName())) {
                node.setError("operands are not of equal type");
            }
        }
    }

    private boolean compareNonOperationalOperands(String leftOperand, String rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    private void getUnApprovedMultiplyOperation(ASTNode node) {
        if (node instanceof MultiplyOperation) {
            ASTNode operandLeft = node.getChildren().get(0);
            ASTNode operandRight = node.getChildren().get(1);
            isColorLiteral(operandLeft, operandRight);
            if (!(operandLeft instanceof ScalarLiteral) && operandRight instanceof Operation) {
                checkIfAllChildrenAreScalar(node);
            } else if (operandRight instanceof AddOperation || operandRight instanceof SubtractOperation) {
                getUnApprovedAddOrSubtractOperation(node);
            }
            if (!(operandLeft instanceof ScalarLiteral) && !(operandRight instanceof ScalarLiteral) && !(operandRight instanceof Operation)) {
                node.setError("Scalar missing");
            }
        }
    }

    private void checkIfAllChildrenAreScalar(ASTNode node) {
        ASTNode operandRight = node.getChildren().get(1);
        if (! ((operandRight.getChildren().get(0)) instanceof ScalarLiteral)) {
            node.setError("Left child not scalar");
        } else if (!(operandRight.getChildren().get(1) instanceof Operation) && !(operandRight.getChildren().get(1) instanceof ScalarLiteral)) {
            node.setError("Right child not scalar");
        } else if (node.getChildren().get(1).getChildren().get(1) instanceof Operation) {
            checkIfAllChildrenAreScalar(node.getChildren().get(1));
        }
    }

    private void getUndefinedVariables(ASTNode node) {
        selectAvailableVariables(node);
        setUndefinedVariablesError(node);
    }

    private void selectAvailableVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            int startOfVariableName = 20;
            String name = node.getNodeLabel().substring(startOfVariableName, node.getNodeLabel().length() - 1);
            ASTNode type = node.getChildren().get(1);
            int index = type.getNodeLabel().lastIndexOf("(") + 1;
            String value = type.getNodeLabel().substring(index, type.getNodeLabel().length() - 1);
            setAvailableVariablesList(name, value);
            setExpressionTypeForHashmap(name, type);
        }
    }

    private void setAvailableVariablesList(String name, String value) {
        HashMap<String, String> hashmapWithValues = new HashMap<>();
        hashmapWithValues.put(name, value);
        variableValues.addFirst(hashmapWithValues);
    }

    private void setExpressionTypeForHashmap(String name, ASTNode type) {
        ExpressionType finalType = ExpressionType.UNDEFINED;
        if (type instanceof ColorLiteral) {
            finalType = ExpressionType.COLOR;
        } else if (type instanceof PixelLiteral) {
            finalType = ExpressionType.PIXEL;
        } else if (type instanceof BoolLiteral) {
            finalType = ExpressionType.BOOL;
        } else if (type instanceof PercentageLiteral) {
            finalType = ExpressionType.PERCENTAGE;
        } else if (type instanceof ScalarLiteral) {
            finalType = ExpressionType.SCALAR;
        }

        HashMap<String, ExpressionType> hashmap = new HashMap<>();
        hashmap.put(name, finalType);
        variableTypes.addFirst(hashmap);
    }

    private void setUndefinedVariablesError(ASTNode node) {
        int startOfVariableName = 20;
        if (node instanceof VariableReference) {
            boolean existingVariable = false;
            for (int i = 0; i < variableTypes.getSize(); i++) {
                String variableKey = node.toString().substring(startOfVariableName, node.getNodeLabel().length());
                if (variableTypes.get(i).containsKey(variableKey)) {
                    existingVariable = true;
                }
            }
            if (!existingVariable) {
                node.setError(node.getNodeLabel() + " is undefined");
            }
        }
    }
}
