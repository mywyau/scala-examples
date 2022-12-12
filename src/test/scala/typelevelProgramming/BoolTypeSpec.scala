//package typelevelProgramming
//
//import specBase.WordSpecScalaMockBase
//
//
//class BoolTypeSpec extends WordSpecScalaMockBase {
//
//  //if it compiles it's correct if it doesnt the type is incorrect
//
//  implicitly[TrueType =:= TrueType]
//  implicitly[FalseType =:= FalseType]
//
//  implicitly[TrueType#Not =:= FalseType]
//  implicitly[FalseType#Not =:= TrueType]
//
//  implicitly[TrueType#Or[TrueType] =:= TrueType]
//  implicitly[TrueType#Or[FalseType] =:= TrueType]
//  implicitly[FalseType#Or[TrueType] =:= TrueType]
//  implicitly[FalseType#Or[FalseType] =:= FalseType]
//
//
//}
