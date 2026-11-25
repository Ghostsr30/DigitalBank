package dev.luan.digitalbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luan.digitalbank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @InjectMocks
    private TransferController controller;

    @Mock
    private TransferService transferService;

    private MockMvc mockMvc;

    private Long sourceId;

    private Long targetId;

    private BigDecimal amount;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        sourceId = 1L;
        targetId = 2L;
        amount = new BigDecimal(50);
    }

    @Test
    void transfer() throws Exception {

        doNothing().when(transferService).transfer(eq(sourceId), eq(targetId), any(BigDecimal.class));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(amount);

        mockMvc.perform(post("/transfers/{sourceId}/{targetId}", sourceId, targetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }


}
