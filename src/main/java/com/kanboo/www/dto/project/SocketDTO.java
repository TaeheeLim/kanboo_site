package com.kanboo.www.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketDTO {
    private String memNick;
    private String memIdx;
    private String prjctIdx;
    private String text;
    private String date;
    private String alarm;
    private String alarmCategory;
    private String calCategory;
    private String textAreaText;
}
