package IndexesCoding;

public abstract class Chooser {
    public static Coding choose(String name) {
        switch (name) {
            case "EliasOmega":
                return new EliasOmega();
            case "EliasGamma":
                return new EliasGamma();
            case "EliasDelta":
                return new EliasDelta();
            case "Fibbo":
                return new Fibbo();
            default:
                return null;
        }
    }
}
