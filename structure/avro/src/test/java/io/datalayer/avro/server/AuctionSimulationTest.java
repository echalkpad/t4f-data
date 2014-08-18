/**
 * Copyright 2011 James Baldassari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.datalayer.avro.server;

import io.datalayer.avro.server.Auction;
import io.datalayer.avro.server.AuctionResult;
import io.datalayer.avro.server.Bidder;
import io.datalayer.avro.server.ConstantBidder;
import io.datalayer.avro.server.DelayInjectingBidder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.avro.util.Utf8;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulates a real-time bidding auction.
 */
public class AuctionSimulationTest extends AbstractBidderTest {
  private static final int NUM_REQUESTS = 10;
  private static final long DELAY_STEP_MILLIS = 100;
  private static final Logger logger = 
      LoggerFactory.getLogger(AuctionSimulationTest.class);
  
  @Test
  public void runAuction() throws Exception {
    // Start servers for each participant in the auction
    // Each server will have a specific constant bid and delay associated
    // with it equal to the bidder index * DELAY_STEP_MILLIS.
    // Bidder 0 will have a delay of 0, Bidder 1 will have a delay of 
    // DELAY_STEP_MILLIS, and so on.
    Map<CharSequence, Bidder.Callback> bidders = 
      new LinkedHashMap<CharSequence, Bidder.Callback>();
    for (int ii = 0; ii < NUM_REQUESTS; ii++) {
      CharSequence bidderId = new Utf8("Bidder " + (ii + 1));
      long constantBid = 100000L * (ii + 1);
      long delayMillis = DELAY_STEP_MILLIS * (ii + 1);
      logger.info("Creating bidder " + bidderId + " with constant bid " + 
          constantBid + " micro-dollars CPM and delay of " + 
          delayMillis + "ms");
      bidders.put(bidderId, startServerAndGetClient(new DelayInjectingBidder(
          new ConstantBidder(SNIPPET, constantBid), delayMillis)));
    }
    
    // Start an auction with a long timeout.
    // All bidders should respond before the auction ends, so 
    // the highest bidder (Bidder 10) should win.
    Auction auction1 = new Auction(bidRequest, bidders, 
        10, TimeUnit.SECONDS);
    AuctionResult result1 = auction1.call();
    if (result1.getIsWon()) {
      logger.info("Winner is: " + result1.getWinningBidderId() + 
          " with a bid of $" + 
          (result1.getWinningBid().getMaxBidMicroCpm() / 1e6) + " CPM.");
    }
    else {
      logger.info("No winners.");
    }
    Assert.assertEquals(bidRequest.getAuctionId(), result1.getAuctionId());
    Assert.assertTrue(result1.getIsWon());
    Assert.assertEquals(new Utf8("Bidder 10"), result1.getWinningBidderId());
    validateBidResponse(bidRequest, result1.getWinningBid());
    
    // Start an auction with a timeout smaller than the delay configured for 
    // some bidders.
    // With 500ms < delay < 600ms, only bidders 1-5 should respond in time.
    // Bidder 5 should win because it has the highest bid of the bidders that 
    // responded before the auction ended.
    Auction auction2 = new Auction(bidRequest, bidders, 
        550, TimeUnit.MILLISECONDS);
    AuctionResult result2 = auction2.call();
    if (result2.getIsWon()) {
      logger.info("Winner is: " + result2.getWinningBidderId() + 
          " with a bid of $" + 
          (result2.getWinningBid().getMaxBidMicroCpm() / 1e6) + " CPM.");
    }
    else {
      logger.info("No winners.");
    }
    Assert.assertEquals(bidRequest.getAuctionId(), result2.getAuctionId());
    Assert.assertTrue(result2.getIsWon());
    Assert.assertEquals(new Utf8("Bidder 5"), result2.getWinningBidderId());
    validateBidResponse(bidRequest, result2.getWinningBid());
    
    // Wait for the rest of the RPCs to complete so that the connections 
    // can be torn down gracefully:
    Thread.sleep(2000L);
  }
}
