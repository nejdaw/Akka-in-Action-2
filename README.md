# Akka-in-Action-2

Study notes from the second edition of Akka in Action.

## Notes

### Chapter 1

Introduction to the concept of an actor as a unit of concurrent programming and Akka framework. The main points of the
chapter are:

1. Akka framework simplifies building applications by proving a single abstraction model.
2. The model provided scales well from single cpu machine to many machines distributed among separate data centers
   forming a cluster.

Chapter also explains the most basic Akka concepts:

1. Actor - 
2. ActorSystem - 
3. Context - 
4. Dispatcher - 
5. Mailbox - 
6. 

### Running the code

You can execute the code in a module by using `sbt` command:

```bash
# Get list of modules
sbt projects

# Run code in 'hello' module
sbt "hello / run"
```
