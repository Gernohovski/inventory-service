package br.com.fatec.mogi.inventory_service.common.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageResponseDTO<T> {

	private List<T> content;

	private Integer size;

	private Integer page;

	private Long totalElements;

	private Integer totalPages;

}
