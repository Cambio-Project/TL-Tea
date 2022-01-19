
[![Maven Build & Test](https://github.com/Cambio-Project/TL-Tea/actions/workflows/maven-build_and_test.yml/badge.svg?branch=main)](https://github.com/Cambio-Project/TL-Tea/actions/workflows/maven-build_and_test.yml)

Maven package@Jitpack  [![](https://jitpack.io/v/Cambio-Project/TL-Tea.svg)](https://jitpack.io/#Cambio-Project/TL-Tea)

## TL-Tea

TL-Tea stands for "**T**emporal **L**ogic - **T**ree **e**valuation and **a**nalysis". With this tool you can turn
Linear Temporal Logic (LTL) or Metric Temporal Logic (MTL) into a tree structure using the parser module. The
interpreter library can then be used to evaluate the tree structure and enhance it with further relations.

## Integration

There are two steps to integrating TL-Tea into your project.

### Maven

1. Add the [Jitpack](https://jitpack.io/) repository to your project.

```
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
```

2. Add the following dependency to your project:

```
    <dependency>
        <groupId>com.github.Cambio-Project</groupId>
        <artifactId>TL-Tea</artifactId>
        <version>0.1.1a</version>
    </dependency>
```

### Gradle

1. Add the [Jitpack](https://jitpack.io/) repository to your project.

```
    url "https://jitpack.io"
```

2. Add the following dependency to your project:

```
    implementation 'com.github.Cambio-Project:TL-Tea:0.1.1a'
```

## Usage

To use TL-Tea simply create a new object of the classes `LTLParser` or `MTLParser` with the formula you'd like to parse
as constructor argument. then call the respective parsing method. This method will return an `ASTNode` object which
represents the root of the abstract syntax tree of your formula. This AST can then be passed to the interpreter for a
pre-evaluation. The resulting `BehaviorInterpretationResult` will contain a simplified AST, the interpretation AST, a
list of listeners that can trigger behavior and a `TriggerNotifier` that can notify you if any left side of an
implication is triggered (set to `true`).
### Example
```
    MTLParser parser = new MTLParser("G(a)->F[10,20](b)");
    ASTNode root = parser.MTL_Formula_File();
    BehaviorInterpretationResult result = Interpreter.interpretAsBehavior(root);
```