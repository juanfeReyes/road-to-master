package r2m.spring.db.spring_jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringJpaApplicationTests {

	@Test
	void contextLoads() {
	}

}
