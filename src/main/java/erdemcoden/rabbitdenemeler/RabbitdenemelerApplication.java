package erdemcoden.rabbitdenemeler;

import com.google.gson.Gson;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitdenemelerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitdenemelerApplication.class, args);
	}

	@Bean
	public StorageProvider storageProvider(JobMapper jobMapper) {
		InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
		storageProvider.setJobMapper(jobMapper);
		return storageProvider;
	}
	@Bean
	public Gson gson() {
		return new Gson();
	}
}
