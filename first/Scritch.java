/**
 *
 * @author beazley
 */
public class Scritch {

    public static void main(String[] args) {
        System.out.println("Hello World!");
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
        
    }
}

interface Exp { 
    static int eval(Exp e) {
        return switch (e) {
            case Num(int value) -> value;       
            case Plus(Exp left, Exp right) -> eval(left) + eval(right);
            default -> 0;
        };
    } 
}
record Num(int value) implements Exp { }
record Plus(Exp left, Exp right) implements Exp { }

class Move {
    Exp steps;
    Move(Exp steps) { this.steps = steps; }
    public String toString() {
        return "Move{steps=" + steps + "}";
    }
    public void run() {
        System.out.printf("Moving %d steps\n", Exp.eval(steps));
    }
    
}
