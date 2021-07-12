package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;
import java.util.HashMap;
import java.util.Map;

public class VariablesHandler implements Handler{
    private final HANLinkedList<HashMap<String, Object>> symbolTable = new HANLinkedList<>();
    Map<String, ExpressionType> literalsMap = new HashMap<>();

    @Override
    public void handle(ASTNode node) {
        setLiteralsMap();
        if (node instanceof VariableAssignment) {
            int startOfVariableName = 20;
            String name = node.getNodeLabel().substring(startOfVariableName, node.getNodeLabel().length() - 1);
            ASTNode type = node.getChildren().get(1);
            setSymbolTable(name, type);
        }
        setUndefinedVariablesError(node);
    }

    private void setLiteralsMap() {
        literalsMap.put("ColorLiteral", ExpressionType.COLOR);
        literalsMap.put("ScalarLiteral", ExpressionType.SCALAR);
        literalsMap.put("PercentageLiteral", ExpressionType.PERCENTAGE);
        literalsMap.put("PixelLiteral", ExpressionType.PIXEL);
        literalsMap.put("BoolLiteral", ExpressionType.BOOL);
    }

    private void setSymbolTable(String name, ASTNode type) {
        HashMap<String, Object> variableDetails = new HashMap<>();
        variableDetails.put("name", name);
        variableDetails.put("value", getValue(type));
        variableDetails.put("type", getType(type));
        symbolTable.addFirst(variableDetails);
    }

    public ExpressionType getType(ASTNode expressionType) {
        ExpressionType type = ExpressionType.UNDEFINED;
        String variableType = expressionType.getClass().getSimpleName();

        if (literalsMap.containsKey(variableType)) {
            type = literalsMap.get(variableType);
        }
        return type;
    }

    private Object getValue(ASTNode expressionType) {
        Literal literal = (Literal) expressionType;
        return literal.value;
    }

    private void setUndefinedVariablesError(ASTNode node) {
        if (node instanceof VariableReference) {
            if (!doesVariableExists(node)) {
                node.setError(node.getNodeLabel() + " is undefined");
            }
        }
    }

    private boolean doesVariableExists(ASTNode node) {
        int startOfVariableName = 20;
        boolean variableExists = false;
        for (int i = 0; i < symbolTable.getSize(); i++) {
            String variableKey = node.toString().substring(startOfVariableName, node.getNodeLabel().length());
            if (symbolTable.get(i).get("name").equals(variableKey)) {
                variableExists =  true;
            }
        }
        return variableExists;
    }

    public HANLinkedList getSymbolTable() {
        return symbolTable;
    }
}
