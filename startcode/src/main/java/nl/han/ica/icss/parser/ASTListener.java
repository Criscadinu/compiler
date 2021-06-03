package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
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
		System.out.println("ENTER STYLESHEET");
		currentContainer.push((new Stylesheet()));
		System.out.println(currentContainer.peek());
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		System.out.println("EXIT STYLESHEET");
		ast.setRoot((Stylesheet) currentContainer.peek());
	}

	@Override
	public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
		System.out.println("ENTER STYLERULE");
		currentContainer.push(new Stylerule());
		System.out.println(currentContainer.peek());
	}

	@Override
	public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
		System.out.println("EXIT STYLERULE");
		Stylerule token = (Stylerule) currentContainer.pop();
		System.out.println(currentContainer.peek());
		currentContainer.peek().addChild(token);
		System.out.println(currentContainer.peek());
	}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		System.out.println("ENTER VARIABLEASSIGNMENT");
		currentContainer.push(new VariableAssignment());
	}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		System.out.println("EXIT VARIABLEASSIGNMENT");
		VariableAssignment token = (VariableAssignment) currentContainer.pop();
		currentContainer.peek().addChild(token);
		System.out.println(currentContainer.peek());
	}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
		System.out.println("ENTER VARIABLEREFERENCE");
		currentContainer.push(new VariableReference(ctx.CAPITAL_IDENT().toString()));
	}

	@Override
	public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
		System.out.println("EXIT VARIABLEREFERENCE");
		VariableReference token = (VariableReference) currentContainer.pop();
		currentContainer.peek().addChild(token);

	}

	@Override
	public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
		System.out.println("EXIT IDSELECTOR");
		IdSelector token = (IdSelector) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		System.out.println("ENTER TAGSELECTOR");
		currentContainer.push(new TagSelector(ctx.LOWER_IDENT().toString()));
		System.out.println(currentContainer.peek());
	}


	@Override
	public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
		System.out.println("EXIT TAGSELECTOR");
		System.out.println(currentContainer.peek());
		TagSelector token = (TagSelector) currentContainer.pop();
		currentContainer.peek().addChild(token);
		System.out.println(currentContainer.peek());
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		System.out.println("ENTER DECLARATION");
		currentContainer.push(new Declaration());
		System.out.println(currentContainer.peek());
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		System.out.println("EXIT DECLARATION");
		System.out.println(currentContainer.peek());
		Declaration token = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
		System.out.println("ENTER IDSELECTOR");
		currentContainer.push(new IdSelector(ctx.ID_IDENT().toString()));
		System.out.println(currentContainer.peek());
	}


	@Override
	public void enterProperty(ICSSParser.PropertyContext ctx) {
		System.out.println("ENTER PROPERTY");
		currentContainer.push(new PropertyName(ctx.LOWER_IDENT().toString()));
		System.out.println(currentContainer.peek());

	}

	@Override
	public void exitProperty(ICSSParser.PropertyContext ctx) {
		System.out.println("EXIT PROPERTY");
		System.out.println(currentContainer.peek());
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
	public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		System.out.println("ENTER COLORLITERAL");
		currentContainer.push(new ColorLiteral(ctx.COLOR().toString()));
		System.out.println(currentContainer.peek());
	}

	@Override
	public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		System.out.println("EXIT COLORLITERAL");
		System.out.println(currentContainer.peek());
		ColorLiteral token = (ColorLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		System.out.println("ENTER PIXELLITERAL");
		currentContainer.push(new PixelLiteral(ctx.PIXELSIZE().toString()));
		System.out.println(currentContainer.peek());
	}

	@Override
	public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		System.out.println("EXIT PIXELLITERAL");
		System.out.println(currentContainer.peek());
		PixelLiteral token = (PixelLiteral) currentContainer.pop();
		currentContainer.peek().addChild(token);
	}

	@Override
	public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
		System.out.println("ENTER BOOLLITERAL");
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
		System.out.println("EXIT BOOLLITERAL");
	}

	public AST getAST() {
		return ast;
    }
}
