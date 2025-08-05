package learn.spring.smart_parking_lot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartParkingLotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartParkingLotApplication.class, args);
		System.out.println("\n=================================================");
		System.out.println("🚗 Smart Parking Lot System Started Successfully!");
		System.out.println("=================================================");
		System.out.println("📊 API Endpoints:");
		System.out.println("   • POST /api/parking/entry - Park a vehicle");
		System.out.println("   • POST /api/parking/exit  - Exit a vehicle");
		System.out.println("   • GET  /api/parking/status - Get parking lot status");
		System.out.println("   • GET  /api/parking/health - Health check");
		System.out.println("\n🌐 Application running on: http://localhost:8080");
		System.out.println("🔍 H2 Console available at: http://localhost:8080/h2-console");
		System.out.println("   - JDBC URL: jdbc:h2:mem:parking_db");
		System.out.println("   - Username: sa");
		System.out.println("   - Password: password");
		System.out.println("=================================================\n");
	}

}
