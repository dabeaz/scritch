# scritchy.py
from dataclasses import dataclass

# Expressions
class Exp: pass

@dataclass
class NumE(Exp):
    value : int

@dataclass
class BoolE(Exp):
    value : bool

@dataclass
class PlusE(Exp):
    left : Exp
    right : Exp

@dataclass
class IfE(Exp):
    test : Exp
    cons : Exp
    alt : Exp

@dataclass
class Let1E(Exp):     # Change this  (let1 name value ...)
    name : str
    value : Exp
    body : Exp

@dataclass
class LookupE(Exp):
    name : str
    
# Values
class Value: pass

@dataclass
class NumV(Value):
    value : int

@dataclass
class BoolV(Value):
    value : int

# Question: How do I change assignment into local binding?  (let)
# Proposed Feature:   (Only one name and value)
#    (let1 name value expression)

variables = { }    # Need multiple environments

# Evaluator
def evaluate(e : Exp, environ: dict) -> Value:
    match e:
        case NumE(value): return NumV(value)
        case BoolE(value): return BoolV(value)
        case LookupE(name): return variables[name]
        case Let1E(name, value, body):
            # What is different about body?  Have to evaluate
            # in a different environment (where the variable is added)
            environ[name] = evaluate(value, environ)
            return evaluate(body, environ)   # Must be in new environment
        case PlusE(left, right):
            return add(evaluate(left, environ), evaluate(right, environ))
        case IfE(test, cons, alt):
            if is_truthy(evaluate(test, environ)):
                return evaluate(cons, environ)
            else:
                return evaluate(alt, environ)
        case _:
            raise RuntimeError(f"Can't evaluate {e}")

# Helpers
def add(left : Value, right : Value) -> Value:
    match (left, right):
        case (NumV(lv), NumV(rv)):
            return NumV(lv + rv)
        case _:
            raise RuntimeError(f"Type error. +")

def is_truthy(v : Value) -> bool:
    match v:
        case BoolV(value):
            return value
        case _:
            return True

def main():
    # Mathematical expression
    exp1 = PlusE(PlusE(NumE(20), NumE(30)), NumE(40))
    print(exp1)
    print(evaluate(exp1), { })

    # Conditional
    exp2 = IfE(BoolE(False), NumE(1), NumE(2))
    print(exp2)
    print(evaluate(exp2), { })


    
