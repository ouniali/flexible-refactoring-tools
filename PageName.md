gap between saved ast and the original ast at the java code editor

# Introduction #
There is a gap between the saved AST and the original one in the code editor. The gap is due to that saving will eliminated unnecessary space (liken tab, space and new line). This will result in error when I try to extract AST node from the saved AST file by using the original line number and column number.

# Details #
The solution to this problem:
1. save the AST not by the CompilationUnit.toString() which will result in the elimination of the space.
2. translate the line number and the column number.