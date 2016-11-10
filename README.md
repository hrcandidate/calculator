# calculator

##Required Build Environment
Maven >=3.0 , Oracle JDK 8
## Building the package
In cloned directory run:
```
mvn package
```

## running the calculator
In cloned directory run:
```
java -jar target/calculator-1.0-SNAPSHOT.jar "add(1,2)"
```

## running application with verbosity

```
java -Dverbosity=ERROR -jar target/calculator-1.0-SNAPSHOT.jar "add(1,2)"
```

