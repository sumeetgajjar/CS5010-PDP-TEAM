package virtualgambling.model.bean;

import java.util.Date;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public class PurchaseInfo {
  private final Share share;
  private final Date date;
  private final long quantity;

  public PurchaseInfo(Share share, Date date, long quantity) {
    this.share = share;
    this.date = date;
    this.quantity = quantity;
  }

  public Share getShare() {
    return share;
  }

  public Date getDate() {
    return date;
  }

  public long getQuantity() {
    return quantity;
  }
}
