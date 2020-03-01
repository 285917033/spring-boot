package com.mopon.system.login.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mopon.system.login.entity.Log;
import com.mopon.system.login.mapper.LogMapper;
import com.mopon.system.login.service.LogService;

/**
 * @author 言曌
 * @date 2019-08-19 21:51
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Integer insert(Log log) {
        return logMapper.insert(log);
    }
}
