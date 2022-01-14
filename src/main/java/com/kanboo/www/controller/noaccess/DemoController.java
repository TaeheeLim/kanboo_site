package com.kanboo.www.controller.noaccess;

import com.kanboo.www.service.inter.project.CompilerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/noAccess")
@RequiredArgsConstructor
public class DemoController {

    private final CompilerService compilerService;

    @PostMapping("/demoCompile")
    public Map<String, String> demoCompile(@RequestParam String code) {
        return compilerService.runDemo(code);
    }


}

