package com.myapp.sabrine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntrepriseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntrepriseDTO.class);
        EntrepriseDTO entrepriseDTO1 = new EntrepriseDTO();
        entrepriseDTO1.setId(1L);
        EntrepriseDTO entrepriseDTO2 = new EntrepriseDTO();
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
        entrepriseDTO2.setId(entrepriseDTO1.getId());
        assertThat(entrepriseDTO1).isEqualTo(entrepriseDTO2);
        entrepriseDTO2.setId(2L);
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
        entrepriseDTO1.setId(null);
        assertThat(entrepriseDTO1).isNotEqualTo(entrepriseDTO2);
    }
}
