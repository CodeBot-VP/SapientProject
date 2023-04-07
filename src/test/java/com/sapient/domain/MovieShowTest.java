package com.sapient.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapient.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovieShowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieShow.class);
        MovieShow movieShow1 = new MovieShow();
        movieShow1.setId(1L);
        MovieShow movieShow2 = new MovieShow();
        movieShow2.setId(movieShow1.getId());
        assertThat(movieShow1).isEqualTo(movieShow2);
        movieShow2.setId(2L);
        assertThat(movieShow1).isNotEqualTo(movieShow2);
        movieShow1.setId(null);
        assertThat(movieShow1).isNotEqualTo(movieShow2);
    }
}
