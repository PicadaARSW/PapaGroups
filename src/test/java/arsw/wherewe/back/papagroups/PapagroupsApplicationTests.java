package arsw.wherewe.back.papagroups;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"MONGODB_URI=mongodb://localhost:27017/testdb"})
class PapagroupsApplicationTests {

	@Test
	void contextLoads() {
		// This test is empty because it is only used to check if the context loads
	}

}
