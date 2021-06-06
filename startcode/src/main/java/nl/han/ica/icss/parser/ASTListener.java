package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.push((new Stylesheet()));
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		ast.setRoot((Stylesheet) currentContainer.peek());
	}

	@Override
	public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
		currentContainer.push(new Stylerule());
	}

	@Override
	public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
		Stylerule token = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		currentContainer.push(new VariableAssignment());
	}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment token = (VariableAssignment) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
		currentContainer.push(new VariableReference(ctx.CAPITAL_IDENT().toString()));
	}

	@Override
	public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
		VariableReference token = (VariableReference) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		currentContainer.push(new TagSelector(ctx.LOWER_IDENT().toString()));
	}

	@Override
	public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
		TagSelector token = (TagSelector) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.push(new Declaration());
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration token = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
		currentContainer.push(new IdSelector(ctx.ID_IDENT().toString()));
	}

	@Override
	public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
		IdSelector token = (IdSelector) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
		currentContainer.push((new ClassSelector(ctx.CLASS_IDENT().toString())));
	}

	@Override
	public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
		ClassSelector token = (ClassSelector) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterProperty(ICSSParser.PropertyContext ctx) {
		currentContainer.push(new PropertyName(ctx.LOWER_IDENT().toString()));
	}

	@Override
	public void exitProperty(ICSSParser.PropertyContext ctx) {
		PropertyName token = (PropertyName) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
		currentContainer.push(new AddOperation());
	}

	@Override
	public void exitAddOperation(ICSSParser.AddOperationContext ctx) {
		AddOperation token = (AddOperation) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		currentContainer.push(new SubtractOperation());
	}

	@Override
	public void exitSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		SubtractOperation token = (SubtractOperation) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		currentContainer.push(new MultiplyOperation());
	}

	@Override
	public void exitMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		MultiplyOperation token = (MultiplyOperation) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterIfClause(ICSSParser.IfClauseContext ctx) {
		currentContainer.push(new IfClause());
	}

	@Override
	public void exitIfClause(ICSSParser.IfClauseContext ctx) {
		IfClause token = (IfClause) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
		currentContainer.push(new ElseClause());
	}

	@Override
	public void exitElseClause(ICSSParser.ElseClauseContext ctx) {
		ElseClause token = (ElseClause) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		currentContainer.push(new ColorLiteral(ctx.COLOR().toString()));
	}

	@Override
	public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		ColorLiteral token = (ColorLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		currentContainer.push(new PixelLiteral(ctx.PIXELSIZE().toString()));
	}

	@Override
	public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		PixelLiteral token = (PixelLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
		if (ctx.TRUE() != null) {
			currentContainer.push(new BoolLiteral(ctx.TRUE().toString()));
		} else {
			currentContainer.push(new BoolLiteral(ctx.FALSE().toString()));
		}
	}

	@Override
	public void exitBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
		BoolLiteral token = (BoolLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		currentContainer.push((new ScalarLiteral(ctx.SCALAR().toString())));
	}

	@Override
	public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		ScalarLiteral token = (ScalarLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	public AST getAST() {
		return ast;
    }
}
