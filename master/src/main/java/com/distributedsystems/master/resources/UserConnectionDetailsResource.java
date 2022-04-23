package com.distributedsystems.master.resources;

import com.distributedsystems.master.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConnectionDetailsResource {

    private String userEmail;

    private String heartBeatUrl;

    private ConnectionStatus connectionStatus;
}
