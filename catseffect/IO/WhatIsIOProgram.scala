//> using scala "2.13.8"
//> using lib "org.typelevel::cats-effect::3.4.8"
//> using lib "org.typelevel::cats-core::2.9.0"

import cats.effect.{ExitCode, IO, IOApp}

object WhatIsIOProgram extends IOApp {
  
   override def run(args: List[String]):IO[ExitCode] = {

      IO.pure(println("Hello world!")).
      flatMap(_ => IO(scala.io.StdIn.readLine("Enter a value:\n"))).
      flatMap(value => IO(println(s"Entered: $value"))).
      map(_ => ExitCode.Success)
    }
  
}
