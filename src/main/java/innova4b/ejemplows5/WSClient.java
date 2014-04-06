package innova4b.ejemplows5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;


public class WSClient 
{
	
	//args0=1 --> get
	//args0=2 --> post
	//args0!={1,2} --> error
    public static void main( String[] args )
    {
    	int opcion=Integer.parseInt(args[0]);
    	if(opcion!=1 && opcion!=2)
    		System.out.println("Opción inválida");
    	else{
    		if (opcion==1)
    			get();
    		else
    			post();
    	}
    }

	private static void get() {
		try {
			URL url = new URL("http://localhost:8080/json/coches/get");
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept","application/json");
				
				if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
					throw new RuntimeException("Error en la conexion. Código de error Http: "+connection.getResponseCode());
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String output;
				while ((output=br.readLine())!=null)
					System.out.println(output);
				
				connection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private static void post() {
		try {
			URL url = new URL("http://localhost:8080/json/coches/post");
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type","application/json");
				
				Coche coche = new Coche();
				coche.setMarca("Volkswagen");
				coche.setModelo("Polo");
				coche.setMatricula("3289DJR");
				coche.setAnyoCompra(2008);
				
				//Uso la librería Jackson para pasar de objeto coche a json
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(coche);

				OutputStream os = connection.getOutputStream();
				os.write(json.getBytes());
				os.flush();
				
				if (connection.getResponseCode()!=HttpURLConnection.HTTP_CREATED){
					throw new RuntimeException("Error al crear el coche. Código de error Http: "+connection.getResponseCode());
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		 
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
				
				connection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}	
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
    
    
    
}
