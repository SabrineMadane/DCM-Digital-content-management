package com.myapp.sabrine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeDTO.class);
        DemandeDTO demandeDTO1 = new DemandeDTO();
        demandeDTO1.setId(1L);
        DemandeDTO demandeDTO2 = new DemandeDTO();
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
        demandeDTO2.setId(demandeDTO1.getId());
        assertThat(demandeDTO1).isEqualTo(demandeDTO2);
        demandeDTO2.setId(2L);
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
        demandeDTO1.setId(null);
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
    }
}
