package com.myapp.sabrine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HRDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HRDTO.class);
        HRDTO hRDTO1 = new HRDTO();
        hRDTO1.setId(1L);
        HRDTO hRDTO2 = new HRDTO();
        assertThat(hRDTO1).isNotEqualTo(hRDTO2);
        hRDTO2.setId(hRDTO1.getId());
        assertThat(hRDTO1).isEqualTo(hRDTO2);
        hRDTO2.setId(2L);
        assertThat(hRDTO1).isNotEqualTo(hRDTO2);
        hRDTO1.setId(null);
        assertThat(hRDTO1).isNotEqualTo(hRDTO2);
    }
}
