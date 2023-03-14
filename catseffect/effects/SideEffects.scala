//> using scala "2.13.8"
import scala.io.StdIn



//side effects are inevitable for userful programs
/**An effect is a concept that bridges our necessity to
 produce side-effects with our desire to write purely
  functional code.
  **/

/**
  Effect-types: Properties:
  -> type signature describes the kind of CALCULATION that will be performed
  -> type signature describes the kind of VALUE that will be calculated
  -> when side effects r needed, effect construction is separate from effect execution
 e.g
  -> Option is an effect type:
     - describes a possibly absent value
     - computes a value of type A, if it exists
     - side effects are not needed, but for Future side effects are needed and 
       effect construction is not separate from effect execution.
       
**/
object Effects extends App {

  /**
     LazyIO as effect-type
     - describes any computation that might produce side effects
     - calculates a value A , if it's successful
     - side effects r required for evaluation of () => A, if so then,
       effect description/construction is separate from effect execution
    */

    case class MyIO[A](unsafeRun: () => A) /**zero lambda expression**/ {
      
      def map[B](f: A => B):MyIO[B] = 
        MyIO(() => f(unsafeRun()))

      def flatMap[B](f: A => MyIO[B]):MyIO[B] = 
        MyIO(() => f(unsafeRun()).unsafeRun())  
    }

    val aLazyIO: MyIO[Int] = MyIO(() => {
      println("I'm writing something....")
      42
    })

    aLazyIO.unsafeRun()

    /**
   - Pure functional program = a big expression computing a value
   - referential transperancy = can replace an expression with its 
                                value without changing behaviour
   - Effect = data type which embodies a computational concept
              (e.g side effects) & is referentially transparent
    MyIO - a data structure that's just a description of a computation,
           it will only be performed when we call unsafeRun on it, and 
           unsafeRun will triggers the sequence of actions.
  */


  val clock: MyIO[Long]  = MyIO(() => System.currentTimeMillis())

  def measure[A](computation: MyIO[A]):MyIO[Long] = for {
    startTime <- clock
    _ <- computation
    finishTime <- clock
  } yield finishTime - startTime

  //clock.flatMap(sT => computation.flatMap(_ => clock.map(fT => fT-sT)))
  /**
    Deconstructing effects manually is hard. Scala & FP free up the mental space
    for us to write complex code easily. Cats effect will simply be a set of tools
    to do that easily.
    */

    def testTimeIO(): Unit = {
      val test = measure(MyIO(() => Thread.sleep(1000)))
      val res = test.unsafeRun()
      println(res)
    }

    testTimeIO()

    def putStrLn(line: String): MyIO[Unit] = MyIO(() => println(line))

    val read: MyIO[String] = MyIO(() => StdIn.readLine())
  /**
    MyIO data type is a bridge b/w imperative programming & FP
    */

    def testConsole(): Unit = {

      val program: MyIO[Unit] = for {
        line1 <- read
        line2 <- read
        _ <- putStrLn(line1 + line2)
      } yield ()

      program.unsafeRun()
    }
    testConsole()
}
