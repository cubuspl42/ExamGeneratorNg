package pl.edu.pg.examgeneratorng;

abstract class Placeholder {
    abstract String repr();


    abstract Kind getKind();


    interface Kind<T> {

        T tryFindKindBy(String symbol);

        default boolean hasSymbol(String symbol) {

            return getSymbol().equals(symbol);
        }

        String getSymbol();
    }
}
