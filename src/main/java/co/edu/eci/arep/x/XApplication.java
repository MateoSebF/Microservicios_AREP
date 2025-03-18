package co.edu.eci.arep.x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class XApplication {

	public static void main(String[] args) {
		if (System.getenv("DB_URI") == null) { // Verifica si la variable ya está en el entorno
            try {
                Dotenv dotenv = Dotenv.load(); // Intenta cargar el archivo .env
                System.setProperty("DB_URI", dotenv.get("DB_URI"));
            } catch (Exception e) {
                System.out.println("No .env file found, using system environment variables.");
            }
		}

        if (System.getenv("COGNITO_CLIENT_SECRET") == null) { // Verifica si la variable ya está en el entorno
            try {
                Dotenv dotenv = Dotenv.load(); // Intenta cargar el archivo .env
                System.setProperty("COGNITO_CLIENT_SECRET", dotenv.get("COGNITO_CLIENT_SECRET"));
            } catch (Exception e) {
                System.out.println("No .env file found, using system environment variables.");
            }
        }
        System.out.println(System.getenv("DB_URI"));
        System.out.println(System.getenv("COGNITO_CLIENT_SECRET"));
		SpringApplication.run(XApplication.class, args);
	}

}
