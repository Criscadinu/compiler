package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import java.util.ArrayList;

public class Checker {
    private IHANLinkedList<ASTNode> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        validateNodes(ast.root.getChildren());
    }

    private void validateNodes(ArrayList<ASTNode> nodes) {
        for(ASTNode node : nodes) {
            if(node instanceof VariableAssignment) {
                for(ASTNode variable : node.getChildren()) {
                    if (variable instanceof VariableReference) {
                        VariableReference reference = (VariableReference) variable;
                        addVariableReferenceToVariableTypes(reference);
                    }
                }
            }
            if (node instanceof VariableReference) {
                for(int i = 0; i < variableTypes.getSize(); i++) {
                    if (!node.getNodeLabel().equals(variableTypes.get(i))) {
                        //Vergelijken met elkaar
                    }
                }
            }
            validateNodes(node.getChildren());
        }
    }

    private void addVariableReferenceToVariableTypes(VariableReference reference) {
        variableTypes.addFirst(reference);
    }
}
