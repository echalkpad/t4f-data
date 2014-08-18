package io.datalayer.avro.server;

import io.datalayer.avro.server.BidRequest;
import io.datalayer.avro.server.BidResponse;
import io.datalayer.avro.server.Bidder;
import io.datalayer.avro.server.BidderError;
import io.datalayer.avro.server.Notification;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.util.Utf8;

/**
 * Decorates an existing Bidder by adding a delay to its response.
 */
public class DelayInjectingBidder implements Bidder {
  private final Bidder chainedBidder;
  private final long delayMillis;
  
  /**
   * Creates a DelayInjectingBidder.
   * @param chainedBidder the bidder to invoke after adding the delay.
   * @param delayMillis the delay to add, in milliseconds.
   */
  public DelayInjectingBidder(Bidder chainedBidder, long delayMillis) {
    if (chainedBidder == null) {
      throw new NullPointerException("chainedBidder is null");
    }
    if (delayMillis < 1) {
      throw new IllegalArgumentException("delayMillis must be >= 1");
    }
    this.chainedBidder = chainedBidder;
    this.delayMillis = delayMillis;
  }
  
  @Override
  public BidResponse bid(BidRequest bidRequest) throws AvroRemoteException,
      BidderError {
    try {
      Thread.sleep(delayMillis);
    } catch (InterruptedException e) {
      throw BidderError.newBuilder().setCode(-1).
        setDescription(new Utf8("Interrupted while injecting delay")).build();
    }
    return chainedBidder.bid(bidRequest);
  }

  @Override
  public void notify(Notification notification) {
    chainedBidder.notify(notification);
  }
  
  @Override
  public boolean ping() throws AvroRemoteException {
    return chainedBidder.ping();
  }
}
