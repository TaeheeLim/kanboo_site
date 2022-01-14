package com.kanboo.www.service.inter.member;

import java.util.Map;

public interface PageRoleCheckService {

    Map<String, Object> checkProject(String token, Long projectIdx);
}
