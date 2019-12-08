package com.ezlinker.app;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: 测试Lua引擎的接口
 */
public class HelloLua extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        return LuaValue.valueOf("HHHHH");
    }

    public HelloLua() {
    }
}
