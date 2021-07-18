package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import java.util.HashMap;

public class AddOrSubtractOperationHandler implements Handler {
    boolean leftOperandIsVariable;
    ASTNode leftOperand;
    ASTNode rightOperand;
    private final HANLinkedList<HashMap<String, Object>> symbolTable;
    VariablesHandler handler;
    MultiplicationOperationHandler multiplicationOperationHandler;

    public AddOrSubtractOperationHandler(VariablesHandler handler) {
        this.handler = handler;
        multiplicationOperationHandler = new MultiplicationOperationHandler(handler);
        this.symbolTable = handler.getSymbolTable();
    }

    @Override
    public void handle(ASTNode node) {
        setOperands(node);
        if (rightOperandIsOperation()) {
            startValidatingOperation(node);
        } else if (oneOrMoreOperandsIsVariableReference()) {
            handler.setVariableTypesInOperation(leftOperand, rightOperand);
            validate(node);
        } else if (!operandsAreEqual()) {
            setError(node);
        }
    }

    private void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

    private boolean rightOperandIsOperation() {
        return rightOperand instanceof Operation;
    }

    private void startValidatingOperation(ASTNode node) {
        if (rightOperandIsAddOrSubtractOperation()) {
            startAddOrSubtractValidation(node);
        }
        if (rightOperandIsMultiplyOperation()) {
            startMultiplicationValidation(node);
        }
    }

    private void startAddOrSubtractValidation(ASTNode node) {
        checkForUnequalChildOperands(node);
    }

    private void checkForUnequalChildOperands(ASTNode node) {
        if (!leftChildOperandIsEqualToLeftOperand() && !oneOrMoreOperandsIsVariableReference()) {
            setError(node);
        }
    }

    private void startMultiplicationValidation(ASTNode node) {
        ASTNode comparisonNode = node.getChildren().get(0);
        ASTNode multiplicationNode = node.getChildren().get(1);
        ExpressionType multiplicationExpressionType = multiplicationOperationHandler.getEvaluatedExpressionType(multiplicationNode);

        compareTypes(node, handler.getTypeOf(comparisonNode), multiplicationExpressionType);
    }

    private void compareTypes(ASTNode node, ExpressionType comparisonNode, ExpressionType multiplicationNode) {
        if (!comparisonNode.toString().equals(multiplicationNode.toString()) && !comparisonNode.toString().equals("SCALAR")) {
            node.setError("Slechte evaluatie");
        }
    }

    private boolean rightOperandIsAddOrSubtractOperation() {
        return rightOperand instanceof AddOperation || rightOperand instanceof SubtractOperation;
    }

    private boolean rightOperandIsMultiplyOperation() {
        return rightOperand instanceof MultiplyOperation;
    }

    private boolean leftChildOperandIsEqualToLeftOperand() {
        ExpressionType leftOperandType = handler.getTypeOf(leftOperand);
        ExpressionType leftOperandTypeOfRightOperand = handler.getTypeOf(rightOperand.getChildren().get(0));
        String name = getVariableNameFrom(rightOperand.getChildren().get(0));

        if (rightOperand.getChildren().get(0) instanceof VariableReference) {
            ExpressionType type = null;
            for (int i = 0; i < symbolTable.getSize(); i++) {
                if (symbolTable.get(i).get("name").equals(name)) {
                    type = (ExpressionType) symbolTable.get(i).get("type");
                }
            }
            return leftOperandType.equals(type);
        }
        return leftOperandType.equals(leftOperandTypeOfRightOperand);
    }

    private String getVariableNameFrom(ASTNode variableReference) {
        int startingIndex = variableReference.toString().indexOf("(") + 1;
        return variableReference.toString().substring(startingIndex, variableReference.getNodeLabel().length());
    }

    private boolean oneOrMoreOperandsIsVariableReference() {
        return leftOperand instanceof VariableReference || rightOperand instanceof VariableReference;
    }

    private boolean operandsAreEqual() {
        return leftOperand.getClass().getSimpleName().equals(rightOperand.getClass().getSimpleName());
    }

    private void validate(ASTNode node) {
        if (handler.getVariableTypesInOperation().size() > 1) {
            compareExpressionTypes(node);
        } else if (oneOrMoreOperandsIsLiteral()) {
            compareExpressionTypeToLiteral(node);
        }
    }

    private boolean oneOrMoreOperandsIsLiteral() {
        return leftOperand instanceof Literal || rightOperand instanceof Literal;
    }

    private void compareExpressionTypes(ASTNode node) {
        if (!handler.getVariableTypesInOperation().get(0).equals(handler.getVariableTypesInOperation().get(1))) {
            setError(node);
        }
    }

    private void setError(ASTNode node) {
        node.setError("Operands are not of equal type");
    }

    private void compareExpressionTypeToLiteral(ASTNode node) {
        String literalType = getTypeOfLiteral();
        if (handler.getVariableTypesInOperation().size() == 0) {
            return;
        }
        if (!handler.getVariableTypesInOperation().get(0).equals(literalType)) {
            setError(node);
        }
    }

    private String getTypeOfLiteral() {
        String literal = setCorrectLiteral();
        int endIndex = literal.indexOf("L");

        return literal.substring(0, endIndex).toUpperCase();
    }

    private String setCorrectLiteral() {
        String literal = "";

        if (leftOperandIsVariable) {
            literal = rightOperand.getClass().getSimpleName();
        } else {
            literal = leftOperand.getClass().getSimpleName();
        }
        return literal;
    }
}
