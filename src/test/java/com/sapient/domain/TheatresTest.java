package com.sapient.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TheatresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theatres.class);
        Theatres theatres1 = new Theatres();
        theatres1.setId(1L);
        Theatres theatres2 = new Theatres();
        theatres2.setId(theatres1.getId());
        assertThat(theatres1).isEqualTo(theatres2);
        theatres2.setId(2L);
        assertThat(theatres1).isNotEqualTo(theatres2);
        theatres1.setId(null);
        assertThat(theatres1).isNotEqualTo(theatres2);
    }
}
