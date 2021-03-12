val addFunction: (Int, Int) => Int = _ + _
val subtractFunction: (Int, Int) => Int = _ - _
val numbers = Seq(1,2,3,4)

// No start value required
println("***** Reduce Left Example *****")
println(s"Sum: ${numbers.reduceLeft(addFunction)}")
println(s"Subtract: ${numbers.reduceLeft(subtractFunction)}")
println("*************")

println("***** Reduce Right Example *****")
println(s"Sum: ${numbers.reduceRight(addFunction)}")
println(s"Subtract: ${numbers.reduceRight(subtractFunction)}")
println("*************")

/*
Order of operation is different depending on left associative or right associative

Left Associative  -- programming forced precedence where precedence of an operations are equal
  1.    (1 + 2) + 3 + 4
  2.    ((1 + 2) + 3) + 4
  3.    (((1 + 2) + 3) + 4)

Answer = 10

Right Associative
  1.    1 + 2 + (3 + 4)
  2.    1 + (2 + (3 + 4))
  3.    (1 + (2 + (3 + 4)))

Answer = 10

  For addition of numbers either left or right is fine because addition and multiplication is
  'commutative' i.e. the order doesn't matter and associative LHS == RHS

  Associativity is only needed when the operators in an expression have the same precedence. Usually + and - have the same precedence.

  In programming languages, the associativity of an operator is a property that determines how operators of the same precedence are grouped in the absence of parentheses.
 */

/*

  Subtraction - not associative but forcing the precedence really doesnt matter you just override to order of operation

  Left Associative
  1.    (1 - 2) - 3 - 4
  2.    ((1 - 2) - 3) - 4
  3.    (((1 - 2) - 3) - 4)

  Answer = -8

Right Associative
  1.    1 - 2 - (3 - 4)
  2.    1 - (2 - (3 - 4))
  3.    (1 - (2 - (3 - 4)))

  Answer = -2

*/