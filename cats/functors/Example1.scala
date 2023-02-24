object Example1 extends App {
                              import cats.Functor
                              val functorInstance = Functor[Option]
                              
                              val fo: Option[Int]=functorInstance.pure(10)
                              val f2=fo.map(fo)(_ + 1)
                              println(f2
                           }
