package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.config.ItemUploadJobConfig;
import br.com.fatec.mogi.inventory_service.coreService.reader.ItemUploadReader;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/core-service/v1/itens")
public record ItemController(ItemService itemService, JobLauncher jobLauncher, ItemUploadJobConfig jobConfig) {

	@PostMapping
	public ResponseEntity<ItemResponseDTO> cadastrarItem(@RequestBody CadastrarItemRequestDTO dto,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.cadastrarItem(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(item);
	}

	@GetMapping
	public ResponseEntity<CustomPageResponseDTO<ItemResponseDTO>> filtrarItems(
			@ModelAttribute ConsultarItemRequestDTO dto, @PageableDefault Pageable pageable,
			@RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var items = itemService.filtrarItems(dto, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(items);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id,
			@RequestBody AtualizarItemRequestDTO dto, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		var item = itemService.atualizar(dto, id);
		return ResponseEntity.status(HttpStatus.OK).body(item);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarItem(@PathVariable Long id, @RequestHeader("X-ACCESS-TOKEN") String accessToken) {
		itemService.deletar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Apenas arquivos CSV são permitidos");
		}

		File tempFile = null;
		try {
			tempFile = File.createTempFile("upload-", ".csv");
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(file.getBytes());
			}

			Resource resource = new FileSystemResource(tempFile);
			ItemUploadReader reader = new ItemUploadReader(resource);
			var step = jobConfig.itemUploadStep(reader);
			Job job = jobConfig.createJob(step);
			JobParameters params = new JobParametersBuilder().addString("fileName", originalFilename)
				.addLong("timestamp", System.currentTimeMillis())
				.addString("tempFilePath", tempFile.getAbsolutePath())
				.toJobParameters();

			jobLauncher.run(job, params);

			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
		finally {
			if (tempFile != null) {
				final File fileToDelete = tempFile;
				new Thread(() -> {
					try {
						Thread.sleep(60000);
						if (fileToDelete.exists()) {
							fileToDelete.delete();
						}
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}).start();
			}
		}
	}
}
