package util.argmanager;
/**
 * The argmanager only accepts primitives
 * So, if want you can add your own classes here to get them directly from the
 * argManager in converted form
 * Use it in the following way:
 * 
 * argManager.addArgType(new argType<[yourNewClass]>(yourNewClass.class){
 * @Override
 * private t StringToObject(String str) {
 *      [yourNewClass] result = [conversion from String to yourNewClass];
 *      return className.cast(result);
 * }
 */
@SuppressWarnings("rawtypes")
public abstract class argType<t> {

    protected Class<t> className;
    private String[] StringClassNames;
    protected String StrClassName;

    /**
     * 
     * @param className
     */
    public argType(Class<t> className, String... classNameAsString) {
        this.className = className;
        StringClassNames = classNameAsString;
    };

    final public arg setBehaviour(String className, String name, String helpText, String detailHelp, boolean hideHelp, String defaultVal) {
        boolean isRightClass = false;
        StrClassName = className;
        for (String pname : StringClassNames) {
            if (pname.equalsIgnoreCase(className)) {
                isRightClass = true;
                break;
            }
        }
        if (isRightClass) {
            arg<t> newArg = new arg<t>(this.className, name, helpText, detailHelp, hideHelp,
                    (this.className.cast(StringToObject(defaultVal))));
            return newArg;
        }
        return null;
    }

    /**
     * returns an java-Object version of the the object which is passed in
     * string-form
     * 
     * @param c
     * @param str
     * @return
     */
    final public t getObject(Class<t> c, String str) {
            return StringToObject(str);
    }

    /**
     * return className.cast([conversion from string to yourClass]);
     * 
     * @param str
     * @return
     */
    abstract t StringToObject(String str);

    final public Class<t> getArgClass() {
        return className;
    }
}
