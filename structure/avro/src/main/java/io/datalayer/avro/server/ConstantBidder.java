package io.datalayer.avro.server;

/**
 * A Bidder that always bids the same amount.
 */
public class ConstantBidder extends AbstractBidder {
  private final long constantBid;
  
  /**
   * Creates a ConstantBidder.
   * @param snippet the creative snippet to return in the each non-zero bid.
   * @param constantBid the amount to bid for every bid response.
   */
  public ConstantBidder(CharSequence snippet, long constantBid) {
    this(null, snippet, constantBid);
  }
  
  /**
   * Creates a ConstantBidder.
   * @param bidderId the bidder ID to set.
   * @param snippet the creative snippet to return in the each non-zero bid.
   * @param constantBid the amount to bid for every bid response.
   */
  public ConstantBidder(CharSequence bidderId, CharSequence snippet, 
      long constantBid) {
    super(snippet, bidderId);
    if (constantBid < 0) {
      throw new IllegalArgumentException("constantBid must be >= 0");
    }
    this.constantBid = constantBid;
  }

  @Override
  protected long generateBid() {
    return constantBid;
  }
}
