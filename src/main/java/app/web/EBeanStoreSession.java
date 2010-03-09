package app.web;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import app.data.Order;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see app.web.Start#main(String[])
 */
public class EBeanStoreSession extends WebSession
{    
    private Order mCurrentCart;

	/**
     * Constructor
     */
	public EBeanStoreSession(Request request)
	{
		super(request);
	}
	
	public Order getCurrentCart()
	{
		return mCurrentCart;
	}

	public void setCurrentCart(Order ord) {
		mCurrentCart=ord;
	}

}
