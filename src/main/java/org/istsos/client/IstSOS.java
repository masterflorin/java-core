package org.istsos.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Realm;
import org.asynchttpclient.Realm.AuthScheme;
import org.asynchttpclient.Response;
import com.google.gson.JsonParser;

/**
 * Main class for initializing IstSOS configuration and Server initialization. 
 * Contains implementation of HTTP asynchronous requests to IstSOS environment.
 *
 */
public class IstSOS{
	
	private HashMap<String, Server> servers = new HashMap<String, Server>(0);
	
	private static volatile IstSOS instance;
	
	private static AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
		
    private IstSOS() { }
    /**
     * Retrieve IstSOS instance
     * @return instance as IstSOS object
     */
    public static IstSOS getInstance() {
        if (instance == null ) {
            synchronized (IstSOS.class) {
                if (instance == null) {
                    instance = new IstSOS();
                }
            }
        }
        return instance;
    }
	/**
	 * Add server object to the collection of servers
	 * @param name as String
	 * @param server as Server instance
	 * @return server added to the collection
	 */
    public Server addServer(String name, Server server) {
        this.servers.put(name, server);
    	return server;
    }
	/**
	 * Initialize a Server with name and url.
	 * @param name as String
	 * @param url as String
	 * @return initialized Server instance
	 */
    public Server initServer(String name, String url) {
        return this.addServer(name, new Server(name, url));
    }
	/**
	 * Initialize a Server with all parameters.
	 * @param name as String
	 * @param url as String
	 * @param user as String
	 * @param password as String
	 * @return initialized Server instance
	 */
    public Server initServer(String name, String url, String user, String password) {
    	return this.addServer(name, new Server(name, url, user, password));
    }
    /**
     * Retrieve Server from the server collection
     * @param name as String
     * @return Server instance
     */
    public Server getServer(String name){
    	return this.servers.get(name);
    }
    /**
     * Retrieve the collection of servers
     * @return Server collection
     */
    public Collection<Server> getServers(){
    	return this.servers.values();
    }


    /**
     * Execute GET request to the istSOS platform.
     * @param url as String
     * @param callback as IstSOSListener
     * @param realm as ArrayList<String>
     */
    protected static void executeGet(String url, final IstSOSListener callback, ArrayList<String> realm){
    	
    	BoundRequestBuilder builder = asyncHttpClient.prepareGet(url);
    	
    	if(realm != null){
			Realm tmpRealm = new Realm.Builder(realm.get(0), realm.get(1))
					.setUsePreemptiveAuth(true)
					.setScheme(AuthScheme.BASIC)
					.build();
    		builder.setRealm(tmpRealm);
    	}
    	
    	builder.execute(new AsyncCompletionHandler<Integer>(){
			
		    @Override
		    public Integer onCompleted(Response response) throws Exception{
		    	
		    	System.out.println("Request executed..");
		    	
		        EventObject eventObject = new EventObject(
		        		Event.REQUEST, 
		        		new JsonParser()
					        .parse(response.getResponseBody())
					        .getAsJsonObject());
        		
        		if(callback != null){
        			callback.onSuccess(eventObject);
        		}
        		
		        return response.getStatusCode();
		    }
		    
		    @Override
		    public void onThrowable(Throwable t){
		    	System.out.println("Request error!!");
		    	System.out.println(t.getMessage());
		    	t.printStackTrace();
		    }
		    
		});
    	
    }
    /**
     * Execute POST request to the istSOS platform.
     * @param url as String
     * @param data as String
     * @param callback as IstSOSListener
     * @param realm as ArrayList<String>
     */
    protected static void executePost(String url, String data, final IstSOSListener callback, ArrayList<String> realm) {
    	
    	BoundRequestBuilder builder = asyncHttpClient.preparePost(url).setBody(data);

    	if(realm != null){
			Realm tmpRealm = new Realm.Builder(realm.get(0), realm.get(1))
					.setUsePreemptiveAuth(true)
					.setScheme(AuthScheme.BASIC)
					.build();
			builder.setRealm(tmpRealm);
    	}
    	
    	builder.execute(new AsyncCompletionHandler<Integer>(){
    		
    		@Override
		    public Integer onCompleted(Response response) throws Exception {
    			
		    	System.out.println("Request executed..");
		    	
		        EventObject eventObject = new EventObject(
		        		Event.REQUEST, 
		        		new JsonParser()
					        .parse(response.getResponseBody())
					        .getAsJsonObject());
        		
        		if(callback != null){
        			callback.onSuccess(eventObject);
        		}
        		
		        return response.getStatusCode();
    			
    		}
    		
		    @Override
		    public void onThrowable(Throwable t){
		    	System.out.println("Request error!!");
		    	System.out.println(t.getMessage());
		    	t.printStackTrace();
		    }
		    
    	});
    	
    	
    }
    /**
     * Execute PUT request to the istSOS platform.
     * @param url as String
     * @param data as String
     * @param callback as IstSOSListener
     * @param realm as ArrayList<String>
     */
    protected static void executePut(String url, String data, final IstSOSListener callback, ArrayList<String> realm) {
    	
    	BoundRequestBuilder builder = asyncHttpClient.preparePut(url).setBody(data);
    	
    	if(realm != null){
			Realm tmpRealm = new Realm.Builder(realm.get(0), realm.get(1))
					.setUsePreemptiveAuth(true)
					.setScheme(AuthScheme.BASIC)
					.build();
			builder.setRealm(tmpRealm);
    	}
    	
    	builder.execute(new AsyncCompletionHandler<Integer>(){
    		
    		@Override
		    public Integer onCompleted(Response response) throws Exception {
    			
		    	System.out.println("Request executed..");
		    	
		        EventObject eventObject = new EventObject(
		        		Event.REQUEST, 
		        		new JsonParser()
					        .parse(response.getResponseBody())
					        .getAsJsonObject());
        		
        		if(callback != null){
        			callback.onSuccess(eventObject);
        		}
        		
		        return response.getStatusCode();
    			
    		}
    		
		    @Override
		    public void onThrowable(Throwable t){
		    	System.out.println("Request error!!");
		    	System.out.println(t.getMessage());
		    	t.printStackTrace();
		    }
		    
    	});
   

    }
    /**
     * Execute DELETE request to istSOS platform.
     * @param url as String
     * @param data as String
     * @param callback as IstSOSListener
     * @param realm as ArrayList<String>
     */
    protected static void executeDelete(String url, String data, final IstSOSListener callback, ArrayList<String> realm){
    	
   	BoundRequestBuilder builder = asyncHttpClient.prepareDelete(url).setBody(data);
    	
    	if(realm != null){
			Realm tmpRealm = new Realm.Builder(realm.get(0), realm.get(1))
					.setUsePreemptiveAuth(true)
					.setScheme(AuthScheme.BASIC)
					.build();
			builder.setRealm(tmpRealm);
    	}
    	
    	builder.execute(new AsyncCompletionHandler<Integer>(){
    		
    		@Override
		    public Integer onCompleted(Response response) throws Exception {
    			
		    	System.out.println("Request executed..");
		    	
		        EventObject eventObject = new EventObject(
		        		Event.REQUEST, 
		        		new JsonParser()
					        .parse(response.getResponseBody())
					        .getAsJsonObject());
        		
        		if(callback != null){
        			callback.onSuccess(eventObject);
        		}
        		
		        return response.getStatusCode();
    			
    		}
    		
		    @Override
		    public void onThrowable(Throwable t){
		    	System.out.println("Request error!!");
		    	System.out.println(t.getMessage());
		    	t.printStackTrace();
		    }
		    
    	});
    	
    }

}