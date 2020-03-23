package DataCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.oanda.v20.primitives.InstrumentName;

import Documenting.Documentor;

/**
 * This class will be used to stream data from the oanada
 * market. It will mostly include prices of specified
 * instruments but it will also calculate averages
 * using historical data.
 * */
public class DataStreamingService {
	private static Documentor documentor = new Documentor();
	
	/**
	 * Test the stream
	 * */
	public static void main(String[] args) {
		try {
			List<InstrumentName> listOfInstruments = new ArrayList<InstrumentName>();
			priceGETStream(listOfInstruments);
		}catch(Exception e) {
			documentor.addError(e.getMessage());
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @param the list of instruments to use for price collection
	 * */
	public static void priceGETStream(List<InstrumentName> listOfInstruments) throws IOException {
	    URL urlForGetRequest = new URL("https://stream-fxpractice.oanda.com//v3/accounts/101-004-13661335-002/pricing/stream?instruments=EUR_USD%2CUSD_CAD");
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	    conection.setRequestProperty("Authorization","Bearer edbe715b87f077cbb4567a4a58f90421-90ac55c43f6c136f2194b70051ff103f");
	 
	    int responseCode = conection.getResponseCode();
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null) {
	        	System.out.println(readLine);
	            response.append(readLine);
	        } 
	        in .close();

	    } else {
	        System.out.println("GET NOT WORKED");
	    }
	}
}
