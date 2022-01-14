package com.kanboo.www.controller.access;

import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.project.CalendarDTO;
import com.kanboo.www.dto.project.ProjectDTO;
import com.kanboo.www.security.JwtSecurityService;
import com.kanboo.www.service.inter.project.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

	private final CalendarService calendarService;
	private final JwtSecurityService jwtSecurityService;

	@GetMapping(value = "/getAllSchedules")
	public List<CalendarDTO> calendarHandler(@RequestParam Map<String, Object> map){
		Long prjctIdx = Long.parseLong(map.get("prjctIdx") + "");
		String token = map.get("token") + "";

		String memTag = jwtSecurityService.getToken(token);
		CalendarDTO calendarDTO = CalendarDTO.builder()
				.member(MemberDTO.builder().memTag(memTag).build())
				.project(ProjectDTO.builder().prjctIdx(prjctIdx).build())
				.build();

		return calendarService.calendarHandler(calendarDTO);
	}

	@PostMapping(value = "/updateSchedule")
	public CalendarDTO updateCalendar(CalendarDTO calendarDTO){
		return calendarService.updateCalendar(calendarDTO);
	}

	@PostMapping(value = "/insertSchedule")
	public CalendarDTO insertSchedule(CalendarDTO calendarDTO){
		return calendarService.insertCalendar(calendarDTO);
	}

	@PostMapping(value = "/deleteSchedule")
	public CalendarDTO deleteSchedule(CalendarDTO calendarDTO){
		return calendarService.deleteCalendar(calendarDTO);
	}
}
