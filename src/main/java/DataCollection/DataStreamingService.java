package DataCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.oanda.v20.primitives.InstrumentName;

<<<<<<< HEAD
import Documenting.SendReport;


=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
/**
 * This class will be used to stream data from the oanada
 * market. It will mostly include prices of specified
 * instruments but it will also calculate averages
 * using historical data.
 * */
public class DataStreamingService {
	
	/**
	 * Test the stream
	 * */
<<<<<<< HEAD
	public static void main(String[] args) {
=======
	public static void main(String[] args) throws IOException {
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
		try {
			List<InstrumentName> listOfInstruments = new ArrayList<InstrumentName>();
			priceGETStream(listOfInstruments);
		}catch(Exception e) {
<<<<<<< HEAD
			SendReport.addError(e.getMessage());
=======
>>>>>>> f1b2841be7f2b9e53df341f8b1d02d3453b75bb7
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
