# argManager
 Manages arguments from the main function in java
 
 ## Usage
 There are only two necessary methods.
 1. call argManager.setBehaviour
    as parameters the method expects strings in the following format:
    
        "[Name]: <type=[Class]> <help=[help]> <detailHelp=[detailHelp]> <hideInHelp=[true/false]> <default=[value]>"
    
    ### Example: 
        "Hi: <Type=Boolean> <help=Says hi> <hideInHelp=false> <default=true>"
    You don't need to add ever parameter here, if there is nothing given the arg defaults to a String with no help, but it will still show up in the help.
 2. call 
    parse(args);
    
 That's it.

# Own classes
Nativly supported classes are: Boolean, Integer, Double, Float, Long
Every unknown class gets converted to String
New classes can be added by calling 

argManager.addArgType(new ArgType(){[Overwrite the StringToObject method]})

### Example:

    argManager.addArgType(new argType<argtest>(argtest.class, "argtest"){
        @Override
        argtest StringToObject(String str) {
            argtest result = new argtest(str);
            return className.cast(result);
        }
    });

### Note: 
You should only modify the ``` argtest result = new argtest(str); ``` line

In setBehaviour, you can simply use this newly added class.
