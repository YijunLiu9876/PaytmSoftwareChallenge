package com.paytm.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_event")
public class UserEvent {

    @PrimaryKeyColumn(
            type = PrimaryKeyType.PARTITIONED)
    String username;
    @PrimaryKeyColumn(
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    String tweetId;

}
