package dev.kk.clickerApp.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalGame {
    String id;
    Integer score;
    Date gameDate;
    String userName;
}
