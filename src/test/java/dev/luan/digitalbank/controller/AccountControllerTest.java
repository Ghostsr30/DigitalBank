package dev.luan.digitalbank.controller;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.repositories.AccountRepository;
import dev.luan.digitalbank.service.AccountService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById() throws Exception {

        Long accountId = 1L;
        AccountDTO dto = new AccountDTO();
        dto.setId(accountId);
        dto.setHolderName("Luan Vedovoto");
        dto.setAccountNumber(432432432L);
        dto.setBalance(new BigDecimal("100.00"));
        dto.setStatus(AccountStatus.ACTIVE);

        when(accountService.findById(accountId)).thenReturn(dto);

        mockMvc.perform(get("/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.holderName").value("Luan Vedovoto"))
                .andExpect(jsonPath("$.balance").value(100.00));


    }




}
