grammar ICSS;

//--- LEXER: ---

//Properties
COLORPROPERTY: 'color';
BACKGROUNDCOLORPROPERTY: 'background-color';
WIDTHPROPERTY: 'width';
HEIGHTPROPERTY: 'height';

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
stylesheet: (variableAssignment | styleRule)*;

variableAssignment
    : variableReference
    ASSIGNMENT_OPERATOR
    (boolLiteral | colorLiteral | pixelLiteral | percentageLiteral | scalarLiteral | operation | variableReference)
    SEMICOLON
    ;

styleRule
    : (classSelector | idSelector | tagSelector)
    OPEN_BRACE
    (declaration | ifClause | variableAssignment)*
    CLOSE_BRACE
    ;

declaration
    : propertyName
      COLON
      (colorLiteral | pixelLiteral | percentageLiteral | variableReference | scalarLiteral | boolLiteral | operation)
      SEMICOLON
    ;

classSelector
    : CLASS_IDENT
    ;

idSelector
    : ID_IDENT
    ;

tagSelector
    : LOWER_IDENT
    ;

boolLiteral
    : TRUE
    | FALSE
    ;

colorLiteral
    : COLOR
    ;

percentageLiteral
    : PERCENTAGE
    ;

pixelLiteral
    : PIXELSIZE
    ;

scalarLiteral
    : SCALAR
    ;

variableReference
    : CAPITAL_IDENT
    ;

propertyName
    : (COLORPROPERTY | BACKGROUNDCOLORPROPERTY | WIDTHPROPERTY | HEIGHTPROPERTY)
    ;

operation
    : (addOperation | subtractOperation | multiplyOperation)
    ;

addOperation
    : (scalarLiteral | pixelLiteral | variableReference | percentageLiteral)
    PLUS
      (scalarLiteral | pixelLiteral | variableReference | percentageLiteral | operation)
    ;

subtractOperation
    : (scalarLiteral | pixelLiteral | variableReference | percentageLiteral)
    MIN
      (scalarLiteral | pixelLiteral | variableReference  | percentageLiteral | operation)
    ;

multiplyOperation
    : (scalarLiteral | pixelLiteral | variableReference | percentageLiteral)
    MUL
      (scalarLiteral | pixelLiteral | variableReference  | percentageLiteral | operation)
    ;

ifClause
    : IF BOX_BRACKET_OPEN variableReference BOX_BRACKET_CLOSE OPEN_BRACE
    (ifClause | declaration | elseClause | variableAssignment)*
    CLOSE_BRACE
    ;

elseClause
    : CLOSE_BRACE ELSE OPEN_BRACE
    declaration
    ;
