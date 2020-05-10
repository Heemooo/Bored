package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;

public class SimpleURL extends URL {

    @Override
    public String content() {
        String content = Bored.of().getEnv().getJetTemplateHelper().parse(this.getContext().getTemplatePath(), this.getCtx());
        this.setContent(content);
        return this.getContent();
    }

    @Override
    public void out() {
        if (StrUtil.isEmpty(this.getContent())) {
            this.setContent(this.content());
        }
        FileUtil.writeBytes(this.getContent().getBytes(CharsetUtil.CHARSET_UTF_8), this.getFullFilePath());
    }
}
