package com.distributedsystems.master.entities;

import com.distributedsystems.master.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "client_connection_details")
public class ClientConnectionDetailsEntity {
    @Id
    private String id;

    @NotBlank(message = "User email is required.")
    @Indexed(unique = true)
    private String userEmail;

    @NotBlank(message = "Heartbeat URL is required.")
    private String heartBeatUrl;

    @NotNull(message = "Connection status is required.")
    private ConnectionStatus connectionStatus;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
}
