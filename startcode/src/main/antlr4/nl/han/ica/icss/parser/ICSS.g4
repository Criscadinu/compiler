grammar ICSS;

//--- LEXER: ---

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
    (boolLiteral | colorLiteral | pixelLiteral | percentageLiteral | scalarLiteral | addOperation | subtractOperation | multiplyOperation)
    SEMICOLON
    ;

styleRule
    : (classSelector | idSelector | tagSelector)
    OPEN_BRACE
    (declaration | ifClause)*
    CLOSE_BRACE
    ;

declaration
    : property
      COLON
      (colorLiteral | pixelLiteral | percentageLiteral | variableReference | scalarLiteral | boolLiteral | addOperation | subtractOperation | multiplyOperation)
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

property
    : LOWER_IDENT
    ;

addOperation
    : (scalarLiteral | pixelLiteral | variableReference)
    PLUS
      (scalarLiteral | pixelLiteral | variableReference | addOperation | subtractOperation | multiplyOperation)
    ;

subtractOperation
    : (scalarLiteral | pixelLiteral | variableReference)
    MIN
      (scalarLiteral | pixelLiteral | variableReference  | addOperation | subtractOperation | multiplyOperation)
    ;

multiplyOperation
    : (scalarLiteral | pixelLiteral | variableReference)
    MUL
      (scalarLiteral | pixelLiteral | variableReference  | addOperation | subtractOperation | multiplyOperation)
    ;

ifClause
    : IF BOX_BRACKET_OPEN variableReference BOX_BRACKET_CLOSE OPEN_BRACE
    (ifClause | declaration | elseClause)*
    CLOSE_BRACE
    ;

elseClause
    : CLOSE_BRACE ELSE OPEN_BRACE
    declaration
    ;

