package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChefTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chef.class);
        Chef chef1 = new Chef();
        chef1.setId(1L);
        Chef chef2 = new Chef();
        chef2.setId(chef1.getId());
        assertThat(chef1).isEqualTo(chef2);
        chef2.setId(2L);
        assertThat(chef1).isNotEqualTo(chef2);
        chef1.setId(null);
        assertThat(chef1).isNotEqualTo(chef2);
    }
}
