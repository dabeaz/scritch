
public class Scritch {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        // Programs: Written in abstract syntax
        var prog1 = new Move(new Num(100));
        System.out.println(prog1);
        prog1.run();
        
        var prog2 = new Move(new Plus(new Num(20), new Num(30)));    
        System.out.println(prog2);
        prog2.run();
        
        var prog3 = new Move(new Plus(
                                new Plus(new Num(20), new Num(30)),
                                new Num(40)));
        System.out.println(prog3);
        prog3.run();    
        
        var prog4 = new Move(new If(new Bool(true),
                                    new Num(1), new Num(2)));
        prog4.run();
    }
}

// Expressions
interface Exp { 
    static Value eval(Exp e) {
        return switch (e) {
            case Num(int value) -> new NumV(value); 
            case Bool(boolean value) -> new BoolV(value);
            case Plus(Exp left, Exp right) -> 
                add(eval(left),eval(right));
            
            //case Mul(Exp left, Exp right) -> 
            //    new NewV(eval(left) * eval(right)));
            case If(Exp test, Exp cons, Exp alt) -> {
                if (isTruthy(eval(test))) {
                     yield eval(cons);
                } else {
                     yield eval(alt);
                }
            }
            default -> throw new RuntimeException("Can't eval that");
        };
    } 
    static Value add(Value left, Value right) {
        if (left instanceof NumV(int lv) &&
            right instanceof NumV(int rv)) {
            return new NumV(lv + rv);
        } else {
            throw new RuntimeException("type error +");
        }    
    }
    static boolean isTruthy(Value val) {
         if (val instanceof BoolV(boolean v)) {
            return v;
        } else {   // Mystery Languages
             return true;
            //throw new RuntimeException("type error. Expected bool");
        }
    }
    
}
record Num(int value) implements Exp { }
record Bool(boolean value) implements Exp { }
record If(Exp test, Exp consequence, Exp alternative) implements Exp { }
record Plus(Exp left, Exp right) implements Exp { }
record Mul(Exp left, Exp right) implements Exp { }

interface Value { 
    static int toJavaInt(Value v) {
        if (v instanceof NumV(int x)) {
            return x;
        } else {
            throw new RuntimeException("type error. Expected integer");
        }
    }
   }
record NumV(int value) implements Value { }
record BoolV(boolean value) implements Value { }



// This is like a "statement". Not a value, but does something.
class Move {
    Exp steps;
    Move(Exp steps) { this.steps = steps; }
    public String toString() {
        return "Move{steps=" + steps + "}";
    }
    public void run() {
        System.out.printf("Moving %d steps\n", Value.toJavaInt(Exp.eval(steps)));
    }
    
}
