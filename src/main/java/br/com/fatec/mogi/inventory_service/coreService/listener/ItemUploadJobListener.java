package br.com.fatec.mogi.inventory_service.coreService.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemUploadJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Iniciando job de upload de itens: {}", jobExecution.getJobParameters().getString("fileName"));
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		long readCount = jobExecution.getStepExecutions().stream().mapToLong(step -> step.getReadCount()).sum();

		long writeCount = jobExecution.getStepExecutions().stream().mapToLong(step -> step.getWriteCount()).sum();

		long skipCount = jobExecution.getStepExecutions().stream().mapToLong(step -> step.getSkipCount()).sum();

		log.info("Job finalizado - Status: {}, Lidos: {}, Salvos: {}, Pulados: {}", jobExecution.getStatus(), readCount,
				writeCount, skipCount);

		jobExecution.getExecutionContext().putLong("totalLidos", readCount);
		jobExecution.getExecutionContext().putLong("totalSalvos", writeCount);
		jobExecution.getExecutionContext().putLong("totalErros", skipCount);
	}

}
