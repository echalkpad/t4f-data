package io.datalayer.avro.server;

import io.datalayer.avro.server.BidRequest;
import io.datalayer.avro.server.BidResponse;
import io.datalayer.avro.server.Bidder;
import io.datalayer.avro.server.BidderError;
import io.datalayer.avro.server.Notification;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for Bidder implementations.
 */
public abstract class AbstractBidder implements Bidder {
  private final CharSequence bidderId;
  private final CharSequence snippet;
  private static final AtomicInteger idGenerator = new AtomicInteger(0);
  private static final Logger logger = LoggerFactory.getLogger(AbstractBidder.class);

  /**
   * Creates an AbstractBidder with the given ID.
   * @param bidderId the ID to set.
   * @param snippet the creative snippet to return in the each non-zero bid.
   */
  public AbstractBidder(CharSequence snippet) {
    this(snippet, null);
  }
  
  /**
   * Creates an AbstractBidder with the given ID.
   * @param snippet the creative snippet to return in the each non-zero bid.
   * @param bidderId the ID to set.
   */
  public AbstractBidder(CharSequence snippet, CharSequence bidderId) {
    if (snippet == null) {
      throw new NullPointerException("snippet is null");
    }
    if (bidderId == null) {
      bidderId = new Utf8("Bidder " + idGenerator.incrementAndGet());
    }
    this.bidderId = bidderId;
    this.snippet = snippet;
  }
  
  /**
   * Gets this bidder's ID.
   * @return the bidder ID.
   */
  public CharSequence getBidderId() {
    return bidderId;
  }
  
  /**
   * Gets the creative snippet that this Bidder returns with non-zero bids.
   * @return the creative snippet.
   */
  public CharSequence getSnippet() {
    return snippet;
  }
  
  /**
   * Generates a bid.
   * @return the bid in units of micro-dollars CPM.
   */
  abstract protected long generateBid();
  
  @Override
  public BidResponse bid(BidRequest bidRequest) 
      throws AvroRemoteException, BidderError {
    logger.debug("Bidder " + getBidderId() + " received request: " + bidRequest);
    BidResponse.Builder bidResponseBuilder = 
        BidResponse.newBuilder().setMaxBidMicroCpm(generateBid());
    
    // If the bidder decided to bid, set the creative snippet:
    if (bidResponseBuilder.getMaxBidMicroCpm() > 0) {
      bidResponseBuilder.setCreativeSnippet(getSnippet());
    }
    
    BidResponse bidResponse = bidResponseBuilder.build();
    logger.debug("Bidder " + getBidderId() + " sending response: " + bidResponse);
    return bidResponse;
  }

  @Override
  public void notify(Notification notification) {
    logger.info(getBidderId() + " received notification: " + notification);
  }
  
  @Override
  public boolean ping() {
    return true;
  }
}
