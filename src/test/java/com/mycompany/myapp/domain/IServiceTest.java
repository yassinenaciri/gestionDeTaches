package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IService.class);
        IService iService1 = new IService();
        iService1.setId(1L);
        IService iService2 = new IService();
        iService2.setId(iService1.getId());
        assertThat(iService1).isEqualTo(iService2);
        iService2.setId(2L);
        assertThat(iService1).isNotEqualTo(iService2);
        iService1.setId(null);
        assertThat(iService1).isNotEqualTo(iService2);
    }
}
