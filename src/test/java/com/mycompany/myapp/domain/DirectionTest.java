package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direction.class);
        Direction direction1 = new Direction();
        direction1.setId(1L);
        Direction direction2 = new Direction();
        direction2.setId(direction1.getId());
        assertThat(direction1).isEqualTo(direction2);
        direction2.setId(2L);
        assertThat(direction1).isNotEqualTo(direction2);
        direction1.setId(null);
        assertThat(direction1).isNotEqualTo(direction2);
    }
}
