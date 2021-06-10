package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;

public class Checker {
    private IHANLinkedList<ASTNode> variableTypes;
    private ArrayList<VariableReference> availableVariables = new ArrayList<>();

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        validateNodes(ast.root.getChildren());
    }

    private void validateNodes(ArrayList<ASTNode> nodes) {
        for(ASTNode node : nodes) {
            getUndefinedVariables(node);
            checkValidOperandsForAddOrSubtractOperation(node);
            checkValidOperandForMultiplyOperation(node);

            validateNodes(node.getChildren());
        }
    }

    private void checkValidOperandsForAddOrSubtractOperation(ASTNode node) {
        if (node instanceof AddOperation | node instanceof SubtractOperation) {
            if(node.getChildren().get(0).getClass().getName() != node.getChildren().get(1).getClass().getName()) {
                node.setError("Operands are not of the same type"); // %literal1 "+" %literal2 + "are not of the same type
            }
        }
    }

    private void checkValidOperandForMultiplyOperation(ASTNode node) {
        ArrayList<ASTNode> scalars = new ArrayList<>();

        if (node instanceof MultiplyOperation) {
            for(ASTNode literal : node.getChildren()) {
                if (literal.getClass().getName().equals("nl.han.ica.icss.ast.literals.ScalarLiteral")) {
                    scalars.add(literal);
                }
            }
            if(scalars.size() == 0) {
                node.setError("At least one scalar literal should be in a multiply operation");
            }
        }
    }

    private void getUndefinedVariables(ASTNode node) {
        selectAvailableVariables(node);
        setUndefinedVariablesError(node);
    }

    private void selectAvailableVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            if (!availableVariables.contains(node)) { // Highlighted node inspecteren
                availableVariables.add((VariableReference) node.getChildren().get(0));
            }
        }
    }

    private void setUndefinedVariablesError(ASTNode node) {
        if (node instanceof VariableReference) {
            if (!availableVariables.contains(node)) {
                node.setError("Variable undefined");
            }
        }
    }
}
