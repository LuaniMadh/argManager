package util.argmanager;

import java.util.ArrayList;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class argManager {

    private static ArrayList<arg> args = new ArrayList<arg>();
    private static ArrayList<argType> argTypes = new ArrayList<argType>();
    private static String helpInto = "";

    public static void setHelpIntro(String nHelp) {
        helpInto = nHelp;
    }

    public static void addArgType(argType a) {
        argTypes.add(a);
    }

    /**
     * Syntax: "[Name]: <type=[Class]> <help=[help]> <hideInHelp=[true/false]>
     * <default=[value]>"
     * Example: "Hi: <Type=Boolean> <help=Says hi> <hideInHelp=false>
     * <default=true>"
     * Nativly supported: Boolean, Integer, Double, Float, Long
     * Every unknown class gets converted to String
     * New classes can be added with addArgType(new ArgType(){[Overwrite the
     * StringToObject method]})
     * 
     * @param behav
     */
    public static void setBehaviour(String... behav) {
        for (String s : behav) {
                int to = s.indexOf(":");
                String n = s.substring(0, (to == -1) ? s.length() : to);
                if (NameIsForBidden(n)) {
                    throw new IllegalArgumentException("The name " + n + " is forbidden");
                }
                String className = "";
                String helpText = "";
                boolean hideHelp = false;
                String defaultVal = "";
                String detailHelp = "";
                int b = s.indexOf("hideInHelp");
                if (b != -1) {
                    String v = s.substring(b + 11 /* hideInHelp= --> 11 letters */, s.indexOf(">", b));
                    hideHelp = v.equalsIgnoreCase("true") || v.equals("t");
                }
                b = s.indexOf("help");
                if (b != -1) {
                    String h = s.substring(b + 5 /* help= --> 5 letters */, s.indexOf(">", b));
                    helpText = h;
                }
                b = s.indexOf("default");
                if (b != -1) {
                    String d = s.substring(b + 8 /* default= --> 8 letters */, s.indexOf(">", b));
                    defaultVal = d;
                }
                b = s.indexOf("detailHelp");
                if (b != -1) {
                    String d = s.substring(b + 11 /* detailHelp= --> 11 letters */, s.indexOf(">", b));
                    detailHelp = d;
                }

                b = s.indexOf("type");
                if (b != -1) {
                    className = s.substring(b + 5 /* type= --> 5 letters */ , s.indexOf(">", b));
                    if (className.equalsIgnoreCase("String") || className.equalsIgnoreCase("str")) {
                        arg<String> newArg = new arg<String>(String.class, n, helpText, detailHelp, hideHelp,
                                defaultVal);
                        args.add(newArg);
                        continue;
                    } else if (className.equalsIgnoreCase("boolean") || className.equalsIgnoreCase("bool")) {
                        arg<Boolean> newArg = new arg<Boolean>(boolean.class, n, helpText, detailHelp, hideHelp,
                                defaultVal.equalsIgnoreCase("true"));
                        args.add(newArg);
                        continue;
                    } else if (className.equalsIgnoreCase("int") || className.equalsIgnoreCase("integer")) {
                        try {
                            arg<Integer> newArg = new arg<Integer>(Integer.class, n, helpText, detailHelp, hideHelp,
                                    Integer.parseInt((defaultVal.isEmpty()) ? "0" : defaultVal));
                            args.add(newArg);
                        } catch (NumberFormatException e) {
                            try {
                                arg<Integer> newArg = new arg<Integer>(Integer.class, n, helpText, detailHelp, hideHelp,
                                        Integer.parseInt((defaultVal.isEmpty()) ? "0" : defaultVal, 16));
                                args.add(newArg);
                            } catch (NumberFormatException f) {
                                throw e;
                            }
                        }
                        continue;
                    } else if (className.equalsIgnoreCase("double")) {
                        arg<Double> newArg = new arg<Double>(double.class, n, helpText, detailHelp, hideHelp,
                                Double.parseDouble((defaultVal.isEmpty()) ? "0" : defaultVal));
                        args.add(newArg);
                        continue;
                    } else if (className.equalsIgnoreCase("float") || className.equalsIgnoreCase("f")) {
                        arg<Float> newArg = new arg<Float>(float.class, n, helpText, detailHelp, hideHelp,
                                Float.parseFloat((defaultVal.isEmpty()) ? "0.0f" : defaultVal));
                        args.add(newArg);
                        continue;
                    } else if (className.equalsIgnoreCase("long")) {
                        arg<Long> newArg = new arg<Long>(long.class, n, helpText, detailHelp, hideHelp,
                                Long.parseLong((defaultVal.isEmpty()) ? "0" : defaultVal));
                        args.add(newArg);
                        continue;
                    } else if (className.equalsIgnoreCase("char") || className.equalsIgnoreCase("character")) {
                        arg<Character> newArg = new arg<Character>(char.class, n, helpText, detailHelp, hideHelp,
                                defaultVal.strip().charAt(0));
                        args.add(newArg);
                        continue;
                    } else {
                        for (argType at : argTypes) {
                            arg buf = at.setBehaviour(className, n, helpText, detailHelp, hideHelp, defaultVal);
                            if (buf != null) {
                                args.add(buf);
                                break;
                            }
                        }
                    }
                } else {
                    // System.out.println("Unknown or unset type: " + n);
                    args.add(new arg<String>(String.class, n, helpText, detailHelp, hideHelp, defaultVal));
                }
        }
    }

    /**
     * Checks if the name is allowed for an argument.
     * 
     * @param n
     * @return
     */
    protected static boolean NameIsForBidden(String n) {
        String[] forbiddenNames = { "help" };
        for (String s : forbiddenNames) {
            if (n.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the argument asked for
     * 
     * @param <t>
     * @param type
     * @param name
     * @return
     */
    public static <t> t getArg(Class<t> type, String name) {
        for (arg a : args) {
            if (a.getName().equals(name)) {
                if (StripClassName(a.getType().getTypeName()).equalsIgnoreCase(StripClassName(type.getTypeName()))) {
                    // System.out.println(((arg<t>) a).toString());
                    return ((arg<t>) a).getValue();
                } else {
                    throw new ArgNotFoundException(
                            "Arg " + name + " found, but it is from class " + a.getType().getTypeName()
                                    + ", and not " + type.getTypeName() + ".");                                    
                }
            }
        }
        throw new ArgNotFoundException("Arg with " + name + " not found");        
    }

    protected static String StripClassName(String n) {
        return n.substring(n.lastIndexOf(".") + 1, n.length());
    }

    /**
     * Returns the arg, but defaults to a String.
     * If no type is given when setting behaviour, the arg is always a String.
     * 
     * @param name
     * @return
     */
    public static String getArg(String name) {
        return getArg(String.class, name);
    }

    public static boolean isSet(String name) {
        for (arg a : args) {
            if (a.getName().equals(name)) {
                return a.isSet();
            }
        }
        throw new ArgNotFoundException("Arg with " + name + " not found");
    }

    /**
     * Tages in the String[] args from the main function and extracts all
     * information needed for the previously with setBehaviour prepared arguments.
     * 
     * @param rawArgs
     */
    public static void parse(String[] rawArgs) {
        if (rawArgs.length == 0) {
            return;
        }
        checkForHelp(rawArgs);
        for (int i = 0; i < rawArgs.length; i++) {
            if (rawArgs[i].startsWith("--")) {
                int argEnd = i + 1;
                for (int k = i + 1; k <= rawArgs.length; k++) {
                    argEnd = k;
                    if (k == rawArgs.length || rawArgs[k].startsWith("--")) {
                        break;
                    }
                }
                String stringRep = "";
                for (int k = i + 1; k < argEnd && k < rawArgs.length; k++) {
                    stringRep += " " + rawArgs[k];
                }
                stringRep = stringRep.strip();

                if (stringRep.startsWith("--")) {
                    stringRep = "";
                }

                String argName = rawArgs[i];
                argName = argName.replaceAll("--", "");

                arg argument = null;
                for (arg a : args) {
                    if (a.getName().equalsIgnoreCase(argName)) {
                        argument = a;
                        break;
                    }
                }
                if (argument == null) {
                    System.err.println("Unknown argument: " + argName);
                    continue;
                }
                String className = argument.getType().toString();
                className = className.substring(className.lastIndexOf(".") + 1, className.length());
                if (className.equalsIgnoreCase("string")) {
                    argument.setValue(stringRep);
                } else if (className.equalsIgnoreCase("boolean") || className.equalsIgnoreCase("bool")) {
                    argument.setValue((stringRep.isEmpty()) ? true : (stringRep.equalsIgnoreCase("true")));
                } else if (className.equalsIgnoreCase("int") || className.equalsIgnoreCase("integer")) {                    
                    argument.setValue((stringRep.isEmpty()) ? 0 : Integer.parseInt((stringRep.startsWith("0x"))?stringRep.substring(2):stringRep, (stringRep.startsWith("0x"))?16:10));                    
                } else if (className.equalsIgnoreCase("double")) {
                    argument.setValue((stringRep.isEmpty()) ? 0 : Double.parseDouble(stringRep));
                } else if (className.equalsIgnoreCase("float") || className.equalsIgnoreCase("f")) {
                    argument.setValue((stringRep.isEmpty()) ? 0.0f : Float.parseFloat(stringRep));
                } else if (className.equalsIgnoreCase("long")) {
                    argument.setValue((stringRep.isEmpty()) ? 0 : Long.parseLong((stringRep.startsWith("0x"))?stringRep.substring(2):stringRep, (stringRep.startsWith("0x"))?16:10));
                } else if (className.equalsIgnoreCase("char") || className.equalsIgnoreCase("character")) {
                    argument.setValue((stringRep.isEmpty()) ? "" : stringRep.strip().charAt(0));
                } else {
                    for (argType at : argTypes) {
                        if (at.getArgClass().toString().equalsIgnoreCase(className)) {
                            argument.setValue(at.getObject(at.getClass(), stringRep));
                        }
                    }
                    System.out.println("Unknown class/type");
                }
            }
        }
    }

    /**
     * There is always
     * 
     * @param rawArgs
     */
    protected static void checkForHelp(String[] rawArgs) {
        if ((rawArgs[0].equalsIgnoreCase("-help") || rawArgs[0].equalsIgnoreCase("--help")) && rawArgs.length == 1) {
            if (helpInto.isEmpty()) {
                System.out.println(helpInto);
            } else {
                System.out.print("\n");
            }

            for (arg a : args) {
                if (a.hideInHelp())
                    continue;
                String className = a.getType().toString();
                className = className.substring(className.lastIndexOf(".") + 1, className.length());
                className = className.substring(className.lastIndexOf(" ") + 1, className.length());
                System.out.println("--" + a.getName() + ((className.equals("String")) ? "" : (" <" + className + ">"))
                        + ": " + a.getHelp());
            }

            System.exit(0);
        } else if ((rawArgs[0].equalsIgnoreCase("-help") || rawArgs[0].equalsIgnoreCase("--help"))
                && rawArgs.length >= 2) {

            String detailHelpArgName = rawArgs[1];
            for (arg a : args) {
                if (detailHelpArgName.equals(a.getName())) {
                    System.out.println(a.getName() + ": " + a.getDetailHelp());
                    System.exit(0);
                }
            }
            System.out.println("Unknown parameter \"" + rawArgs[1] + "\"");
            System.exit(0);
        }
    }
}