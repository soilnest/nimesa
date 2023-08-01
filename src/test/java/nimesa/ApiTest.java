package nimesa;
import java.util.Scanner;
import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiTest {

	public Response apiGET() {
		Response res = given().when().get(
				"https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22");
		return res;
	}
	
	public int getListIndex(JsonPath jvalue, String dateTS) {
		int index = 0;
		int size = jvalue.getInt("list.size()");
		loop: for (int i = 0; i < size; i++) {
			String date = jvalue.getString("list[" + i + "].dt_txt");
			String[] val = date.split(" ");
			if (val[0].equalsIgnoreCase(dateTS)) {
			index = i;
			break loop;
			}
		}
		return index;
	}

	@Test
	public float getTemp(String dateTS) {
		float temperature = 0;
		Response res = apiGET();
		JsonPath j = new JsonPath(res.asString());
		int index = getListIndex(j, dateTS);
		temperature = (j.getFloat("list[" + index + "].main.temp"));
		return temperature;
	}

	@Test
	public float getWind(String dateTS) {
		float windSpeed = 0;
		Response res = apiGET();
		JsonPath j = new JsonPath(res.asString());
		int index = getListIndex(j, dateTS);
		windSpeed = (j.getFloat("list[" + index + "].wind.speed"));
		return windSpeed;
	}

	@Test
	public float getPressure(String dateTS) {
		float windPressure = 0;
		Response res = apiGET();
		JsonPath j = new JsonPath(res.asString());
		int index = getListIndex(j, dateTS);
		windPressure = (j.getFloat("list[" + index + "].main.pressure"));
		return windPressure;
	}

	public static void main(String[] args) {
		int option;
		String dateStr = "";
		ApiTest apiT = new ApiTest();
		Scanner scan = new Scanner(System.in);
		System.out.println("*** Welcome to London Weather Station *** \n");
		do {
			System.out.println("You have the following choices: \n" + "1: Get Temperature \n" + "2: Get Wind Speed \n"
					+ "3: Get Pressure \n" + "0: Exit \n" + "Enter an option: ");
			option = scan.nextInt();

			if (option == 1 || option == 2 || option == 3) {
				System.out.println("Enter a data between 2019-03-27 and 2019-03-31 in yyyy-MM-dd format:");
				dateStr = scan.next();
				System.out.println("Fetching data .....");
				switch (option) {
				case 1:
					System.out.println(
							"=======\n The Temperature is: " + apiT.getTemp(dateStr) + "\n=======");
					break;
				case 2:
					System.out.println(
							"=======\n The Wind Speed is: " + apiT.getWind(dateStr) + "\n=======");
					break;
				case 3:
					System.out.println(
							"=======\n The Pressure is: " + apiT.getPressure(dateStr) + "\n=======");
					break;
				 default: 
					 System.out.println("Invalid operation"); 
					 break;
				}
			} else if (option == 0) {
				System.out.println("Exiting...");
				break;
			}
		} while (option != 0);
	}
}
