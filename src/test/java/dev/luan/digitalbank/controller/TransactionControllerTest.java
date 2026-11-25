package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.DTO.TransactionDTO;
import dev.luan.digitalbank.domain.TransactionType;
import dev.luan.digitalbank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController controller;

    @Mock
    private TransactionService service;

    private MockMvc mockMvc;

    private Long accountId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        accountId = 1L;
    }

    @Test
    void extract() throws Exception{

        TransactionDTO dto = new TransactionDTO();
        dto.setId(accountId);
        dto.setType(TransactionType.DEPOSIT);
        dto.setAmount(new BigDecimal(50));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setTargetAccountId(2L);

        List<TransactionDTO> list = List.of(dto);

        when(service.extract(accountId)).thenReturn(list);

        mockMvc.perform(get("/transactions/{id}/extract", accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("[0].amount").value("50"));

    }
}
