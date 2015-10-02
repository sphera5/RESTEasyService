package mx.com.extend.semarnat.foresta.wsclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import mx.com.extend.semarnat.foresta.data.Student;
import mx.com.extend.semarnat.foresta.data.StudentWrapper;
import mx.com.extend.semarnat.foresta.util.StringListConverter;

public class StudentWSClient {

	private Logger logger = Logger.getLogger(this.getClass());
	private StringListConverter converter = new StringListConverter();

	// Default URL
	//private String BASE_URL = "http://localhost:8080/RestWebServiceTest/rs/json";
	private String BASE_URL = "http://104.44.129.37/RESTEasyService-0.0.1/rs/json";

	private Properties properties = new Properties();

	private void initProperties() {
		InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");

		if (is != null) {
			try {
				properties.load(is);

				BASE_URL = properties.getProperty("student.restws.url");

			} catch (IOException e) {
				logger.error("Error when reading properties: ", e);
				throw new RuntimeException("Can not load application.properties file.");
			}
		} else {
			logger.error("Error when finding application.properties.");
			throw new RuntimeException("Error when finding application.properties.");
		}
	}

	/**
	 * Retrieve over 20 years
	 *
	 * @param anAge
	 * @return
	 */
	public List<Student> getStudentsOverAge(String anAge) {

		Client client = ClientBuilder.newClient();
		WebTarget myResource = client.target(BASE_URL + "/overAge").path(anAge);

		StudentWrapper wrapper = null;
		try {

			wrapper = myResource.request(MediaType.APPLICATION_JSON).get(StudentWrapper.class);

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return wrapper.getList();
	}

	/**
	 * Students by name
	 *
	 * @param studentNames
	 * @return
	 */
	public List<Student> getStudentsByName(List<String> studentNames) {

		Client client = ClientBuilder.newClient();
		WebTarget myResource = client.target(BASE_URL + "/studentsByName").queryParam("names",
				converter.toString(studentNames));

		StudentWrapper wrapper = null;
		try {

			wrapper = myResource.request(MediaType.APPLICATION_JSON).get(StudentWrapper.class);

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return wrapper.getList();
	}

	/**
	 *
	 * @param studentNames
	 * @return
	 */
	public List<Student> getStudentsByNameJson(List<String> studentNames) {

		Client client = ClientBuilder.newClient();
		String nameJson = converter.toJson(studentNames);
		WebTarget myResource = client.target(BASE_URL + "/studentsByNameJson");

		StudentWrapper wrapper = null;
		try {

			wrapper = myResource.request(MediaType.APPLICATION_JSON).put(Entity.text(nameJson), StudentWrapper.class);

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return wrapper.getList();
	}

}
