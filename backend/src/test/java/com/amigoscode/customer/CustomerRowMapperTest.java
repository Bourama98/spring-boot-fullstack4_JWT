package com.amigoscode.customer;


import org.junit.jupiter.api.Test;


import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Alice");
        when(rs.getString("email")).thenReturn("alice@gmail.com");
        when(rs.getInt("age")).thenReturn(19);
        when(rs.getString("gender")).thenReturn("FEMALE");


        // When
Customer actual = customerRowMapper.mapRow(rs, 1);

        // Then
        Customer expected = new Customer(
                1,
                "Alice",
                "alice@gmail.com",
                19,
                Gender.FEMALE);
        assertThat(actual).isEqualTo(expected);
    }
}