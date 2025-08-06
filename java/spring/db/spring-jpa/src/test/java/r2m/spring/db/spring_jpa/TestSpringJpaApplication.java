package r2m.spring.db.spring_jpa;

import org.springframework.boot.SpringApplication;

public class TestSpringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringJpaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
