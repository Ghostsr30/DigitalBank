package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @InjectMocks
    private TransferController controller;

    @Mock
    private TransferService transferService;

    private MockMvc mockMvc;

    private Long accountId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        accountId = 1L;
    }



}
