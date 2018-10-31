package freecell.model;

/**
 * <code>FreecellOperationsBuilder</code> provides functionality to build a
 * <code>FreecellOperations</code> instance using a various combinations of number of cascades and
 * number of open piles.
 */
public interface FreecellOperationsBuilder {

  /**
   * Changes the value in the current FreecellOperationsBuilder with a particular value of cascade
   * pile count.
   *
   * @param c number of cascade piles
   * @return FreecellOperationsBuilder with a changed value of cascade piles.
   */
  FreecellOperationsBuilder cascades(int c);

  /**
   * Changes the value in the current FreecellOperationsBuilder with a particular value of open pile
   * count.
   *
   * @param o number of open piles
   * @return FreecellOperationsBuilder with a changed value of open piles.
   */
  FreecellOperationsBuilder opens(int o);

  /**
   * Use the current state of number of cascade and open pile counts to return a newly constructed
   * <code>FreecellOperations</code> with that many cascade and open piles.
   *
   * @param <K> the type of the card housed in <code>FreeCellOperations</code>
   * @return <code>FreecellOperations</code> with the current state of cascade and open pile counts
   */
  <K> FreecellOperations<K> build();
}
