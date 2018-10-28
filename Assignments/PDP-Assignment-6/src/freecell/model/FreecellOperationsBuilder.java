package freecell.model;

public interface FreecellOperationsBuilder {

  FreecellOperationsBuilder cascades(int c);

  FreecellOperationsBuilder opens(int o);

  <K> FreecellOperations<K> build(); // todo ask on piazza and explain this change
}
