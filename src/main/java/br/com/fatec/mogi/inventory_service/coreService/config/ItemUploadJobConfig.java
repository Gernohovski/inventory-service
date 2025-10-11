package br.com.fatec.mogi.inventory_service.coreService.config;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.listener.ItemUploadJobListener;
import br.com.fatec.mogi.inventory_service.coreService.listener.ItemUploadSkipListener;
import br.com.fatec.mogi.inventory_service.coreService.processor.ItemUploadProcessor;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ItemUploadJobConfig {

	private final JobRepository jobRepository;

	private final PlatformTransactionManager transactionManager;

	private final ItemUploadProcessor processor;

	private final ItemRepository itemRepository;

	private final ItemUploadJobListener jobListener;

	private final ItemUploadSkipListener skipListener;

	public Step itemUploadStep(ItemStreamReader<ItemUploadRequestDTO> reader) {
		return new StepBuilder("itemUploadStep", jobRepository)
			.<ItemUploadRequestDTO, Item>chunk(20, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(itemWriter())
			.faultTolerant()
			.skip(DataIntegrityViolationException.class)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.listener(skipListener)
			.build();
	}

	@Bean
	public ItemWriter<Item> itemWriter() {
		return items -> itemRepository.saveAll(items);
	}

	public Job createJob(Step step) {
		return new JobBuilder("itemUploadJob-" + System.currentTimeMillis(), jobRepository).start(step)
			.listener(jobListener)
			.build();
	}

}