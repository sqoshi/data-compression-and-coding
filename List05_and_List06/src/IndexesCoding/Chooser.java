package IndexesCoding;

public abstract class Chooser {
    public static Elias choose(String name) {
        switch (name) {
            case "Elias Omega":
                return new EliasOmega();
            case "Elias Gamma":
                return new EliasGamma();
            case "Elias Delta":
                return new EliasDelta();
            case "Fibbo":
                return new Fibbo();
            default:
                return null;
        }
    }
}
