package org.jboss.brms.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.jboss.brms.facts.PromotionalOffer;
import org.jboss.brms.facts.Purchase;

public class PromotionTestUtil {

	private static double promotionLevel1;
	private static double promotionLevel2;
	
	public static Collection<PromotionalOffer> getOffers(){
		Random random = new Random( System.currentTimeMillis() );
		promotionLevel1 = random.nextDouble() * 1000;
		promotionLevel2 = random.nextDouble() * 1000;
		
		System.out.println( promotionLevel1 + " " + promotionLevel2);
		
		// TODO provide a JPA implementation
		
		Collection<PromotionalOffer> offers = new ArrayList<>();
		offers.add( new PromotionalOffer(10002, promotionLevel1, 10.00));
		offers.add( new PromotionalOffer(21228, promotionLevel2, 10.00));
		return offers;
	}
	
	public static Collection<Object> getPurchases(){
		Collection<Object> purchases = new ArrayList<>();
		purchases.add( new Purchase(10002, promotionLevel1 + 20) );
		purchases.add( new Purchase(10002, promotionLevel1 - 20) );
		purchases.add( new Purchase(21228, promotionLevel2 + 20) );
		purchases.add( new Purchase(21228, promotionLevel2 - 20) );
		return purchases;
	}
	
}
