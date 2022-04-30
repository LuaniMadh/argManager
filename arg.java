package util.argmanager;

public class arg<t> {

    public String getName() {
        return name;
    }

    public t getValue() {
        return (value == null) ? defaultValue : value;
    }

    public void setValue(t nValue) {
        value = nValue;
        isSet = true;
    }

    public boolean isSet(){
        return isSet;
    }

    public String getHelp() {
        return help;
    }

    public Class<t> getType() {
        return type;
    }

    public String getDetailHelp() {
        return (detailHelp.isEmpty())?help:detailHelp;
    }

    private String name, help;
    private t value;
    private Class<t> type;
    private t defaultValue;
    private boolean hideInHelp;
    private String detailHelp;
    private boolean isSet = false;

    public arg(Class<t> type, String name, t value, t defaultValue, String help, String detailHelp,
            boolean hideInHelp) {
        this.name = name;
        this.help = help;
        this.value = value;
        this.hideInHelp = hideInHelp;
        this.defaultValue = defaultValue;
        this.type = type;
        this.detailHelp = detailHelp;
    }

    public arg(Class<t> type, String name, String help, String detailHelp, boolean hideInHelp, t defaultValue) {
        this.name = name;
        this.help = help;
        this.hideInHelp = hideInHelp;
        this.defaultValue = defaultValue;
        this.type = type;
        this.detailHelp = detailHelp;
    }

    @Override
    @SuppressWarnings({"rawtypes" ,"unchecked"})
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            if (((arg) obj).getType().equals(type)) {
                arg<t> o = (arg<t>) obj;
                if (value.equals(o.getValue())) {
                    return true;
                } else {
                    return false;
                }
            }
        } 
        return false;
    }

    @Override
    public String toString() {
        return (name + ": <type=" + type.toString() + "> <help=" + help + "> <hideInHelp="
                + ((hideInHelp) ? "true" : "false") + "> <default=" + defaultValue.toString() + ">");
    }

    public boolean hideInHelp() {
        return hideInHelp;
    }
}
