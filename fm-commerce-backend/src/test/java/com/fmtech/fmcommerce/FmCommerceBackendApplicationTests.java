package com.fmtech.fmcommerce;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FmCommerceBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void shouldCallSpringApplicationRun() {

		try (MockedStatic<SpringApplication> mocked =
				mockStatic(SpringApplication.class)) {

			FmCommerceBackendApplication.main(new String[]{});

			mocked.verify(
				() -> SpringApplication.run(
					FmCommerceBackendApplication.class,
					new String[]{}
				),
				times(1)
			);
    }
}

}
