/**
 *
 */
package co.com.bancolombia.dynamondb.enums;

import java.util.Arrays;

/**
 * @author linkott
 *
 */
public enum TypeCatalogue {
    C("catalogue"), CD("citydepartment"), CC("citycoverage"), P("plan");

    private String type;

    TypeCatalogue(String typeCatalog) {
        type = typeCatalog;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    public static TypeCatalogue fromEnumType(String s) throws IllegalArgumentException {


        return Arrays.stream(TypeCatalogue.values()).filter(v -> v.type.equals(s))
                .findFirst().orElse(TypeCatalogue.C);
    }

}

