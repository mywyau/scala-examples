

val testSeq: Seq[(String, Int)] =
  Seq(
    ("a", 1),
    ("a", 2),
    ("b", 1),
    ("c", 1),
    ("d", 1),
    ("e", 1),
    ("f", 1),
    ("a", 57)
  )

//println(testSeq.groupBy(identity))


val duplicates: Seq[String] = testSeq.map(_._1).diff(testSeq.map(_._1).distinct).distinct

testSeq.collect{ case tuple: (String, Int) if duplicates.contains(tuple._1)  =>
  tuple
}

//println(testSeq.groupBy(identity).collect {
//  case (x, ys) if ys.map(_._1).lengthCompare(1) > 0 =>
//  println(x)
//  println(ys)
//  x
//})